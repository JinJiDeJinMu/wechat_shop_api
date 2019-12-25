package com.chundengtai.base.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chundengtai.base.bean.*;
import com.chundengtai.base.constant.CacheConstant;
import com.chundengtai.base.event.DistributionEvent;
import com.chundengtai.base.exception.BizErrorCodeEnum;
import com.chundengtai.base.exception.BizException;
import com.chundengtai.base.jwt.JavaWebToken;
import com.chundengtai.base.utils.BeanJwtUtil;
import com.chundengtai.base.weixinapi.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 分销系统应用服务
 */
@Service
@Slf4j
public class DistributionService {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //用户层级服务
    @Autowired
    private CdtDistributionLevelService distributionLevelService;

    //用户服务类
    @Autowired
    private UserService userService;

    //订单服务类
    @Autowired
    private OrderService orderService;

    //分销比例
    @Autowired
    private CdtDistrimoneyService distrimoneyService;

    //用户购买分销得钱记录
    @Autowired
    private CdtDistridetailService distridetailService;

    //分销记录表
    @Autowired
    private CdtRebateLogService rebateLogService;

    //用户分布
    @Autowired
    private CdtUserDistributionService userDistributionService;

    private CdtDistrimoney distrimoney;

    @PostConstruct
    public void initialize() {
        if (distrimoney == null) {
            distrimoney = getDistrimoney();
        }
    }
//功能方法

    private String encryt(Object logModel) {
        try {
            Map chain = BeanJwtUtil.javabean2map(logModel);
            return JavaWebToken.createJavaWebToken(chain);
        } catch (Exception e) {
            log.error("jwt加密异常========");
            e.printStackTrace();
        }
        return "";
    }

    private void changeDistridetailStatus(Order order, CdtDistridetail distridetail) {
        if (!order.getOrderStatus().equals(OrderStatusEnum.COMPLETED_ORDER.getCode())) {
            distridetail.setStatus(DistributionStatus.NON_COMPLETE_ORDER.getCode());
        } else if (order.getOrderStatus().equals(OrderStatusEnum.COMPLETED_ORDER.getCode()) &&
                order.getGoodsType().equals(GoodsTypeEnum.ORDINARY_GOODS.getCode())
        ) {
            int daysNum = Period.between(LocalDateTime.now().toLocalDate(), order.getConfirmTime().toLocalDate()).getDays();
            if (daysNum < 7) {
                distridetail.setStatus(DistributionStatus.NOT_SERVEN_ORDER.getCode());
            } else {
                distridetail.setStatus(DistributionStatus.COMPLETED_ORDER.getCode());
            }
        } else if (order.getOrderStatus().equals(OrderStatusEnum.COMPLETED_ORDER.getCode()) &&
                (order.getGoodsType().equals(GoodsTypeEnum.WRITEOFF_ORDER.getCode()) ||
                        order.getGoodsType().equals(GoodsTypeEnum.EXPRESS_GET.getCode()
                        ))) {
            distridetail.setStatus(DistributionStatus.COMPLETED_ORDER.getCode());
        } else if (order.getOrderStatus().equals(OrderStatusEnum.REFUND_ORDER.getCode())) {
            distridetail.setStatus(DistributionStatus.REFUND_ORDER.getCode());
        }
    }

    //返佣比例配置信息获取
    public CdtDistrimoney getDistrimoney() {
        CdtDistrimoney model = (CdtDistrimoney) redisTemplate.opsForValue().get(CacheConstant.SERVICE_CDT_DISTRIMONEY);
        if (model == null) {
            model = distrimoneyService.getOne(new QueryWrapper<CdtDistrimoney>()
                    .lambda().eq(CdtDistrimoney::getStatus, OnOffEnum.OFF.getCode()).orderByDesc(CdtDistrimoney::getId));
            if (model != null) {
                redisTemplate.opsForValue().set(CacheConstant.SERVICE_CDT_DISTRIMONEY, model, 30, TimeUnit.DAYS);
            }
        }
        return model;
    }

    private BigDecimal computeFirstMoney(BigDecimal totalMoney) {
        return totalMoney.multiply(distrimoney.getFirstPartner());
    }

    private BigDecimal computeSecondMoney(BigDecimal totalMoney) {
        return totalMoney.multiply(distrimoney.getSecondPercent());
    }
    //返佣比例配置结束

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void distributionLogic(DistributionEvent event) {
        log.info(" testAsync 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());
        //todo:对推荐信息解密
        if (StringUtils.isEmpty(event.getEncryptCode())) {
            return;
        }

        if (event.getEncryptCode().contains("_")) {
            String promotionId = event.getEncryptCode().split("_")[0];
            Long parentId = Long.valueOf(promotionId);
            if (event.getUserId().equals(parentId)) {
                log.warn("自己不能推荐自己");
                return;
            }

            //判断两人关系是否已经绑定
            Integer count = distributionLevelService.count(new QueryWrapper<CdtDistributionLevel>().lambda()
                    .eq(CdtDistributionLevel::getUserId, event.getUserId())
                    .eq(CdtDistributionLevel::getParentId, parentId)
            );
            if (count >= 1) {
                return;
            }

            //查询推荐人的上级
            CdtDistributionLevel parentModel = distributionLevelService.getOne(new QueryWrapper<CdtDistributionLevel>().lambda().eq(CdtDistributionLevel::getUserId, parentId));
            CdtDistributionLevel model = new CdtDistributionLevel();
            model.setUserId(event.getUserId());
            model.setParentId(parentId);
            model.setSponsorId(parentId);

            //绑定user表层架关系
            LambdaUpdateWrapper<User> condition = new LambdaUpdateWrapper<>();
            condition.set(User::getFirstLeader, parentId);
            condition.set(User::getIsDistribut, TrueOrFalseEnum.TRUE.getCode());
            condition.eq(User::getId, event.getUserId());
            condition.eq(User::getFirstLeader, 0);
            condition.eq(User::getSecondLeader, 0);

            if (parentModel != null) {
                model.setSponsorId(parentModel.getSponsorId());
                condition.eq(User::getSecondLeader, parentModel.getParentId());
            }
            boolean result = distributionLevelService.save(model);
            boolean rows = userService.update(condition);
            log.info("====绑定影响行数====" + rows);
        }
    }

    /**
     * 绑定用户关系
     *
     * @param userId         the user id
     * @param referrerEncpyt the referrer encpyt
     */
    public void referreRelation(long userId, String referrerEncpyt) {
        DistributionEvent event = new DistributionEvent();
        event.setEncryptCode(referrerEncpyt);
        event.setUserId(userId);
        eventPublisher.publishEvent(event);
    }

    /**
     * 记录用户所挣钱
     */
    public boolean recordEarning(int userId, int goldUserId, BigDecimal money, Order order) {
        CdtDistridetail distridetail = new CdtDistridetail();
        distridetail.setUserId(userId);
        distridetail.setGoldUserId(goldUserId);
        distridetail.setMoney(money);
        distridetail.setOrderSn(order.getOrderSn());
        distridetail.setCreatedTime(new Date());
        changeDistridetailStatus(order, distridetail);

        distridetail.setToken(encryt(distridetail));
        boolean result = distridetailService.save(distridetail);

        return result;
        //更新用户分销参数信息
    }

    /**
     * 记录分销日志
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void recordDistributeLog(Integer userId, Order order) {
        User user = userService.getById(userId);

        CdtRebateLog logModel = new CdtRebateLog();
        logModel.setBuyUserId(user.getId());
        logModel.setOrderId(order.getId());
        logModel.setOrderSn(order.getOrderSn());

        logModel.setGoldUserId(user.getFirstLeader());
        logModel.setGoodsPrice(order.getAllPrice());
        logModel.setMechantId(order.getMerchantId());
        logModel.setNickname(user.getNickname());
        logModel.setStatus(order.getOrderStatus());
        logModel.setMechantId(order.getMerchantId());
        logModel.setLevel(1);
        logModel.setRemark("一级返佣结算");
        BigDecimal earnMoney = computeFirstMoney(order.getAllPrice());
        logModel.setMoney(earnMoney);
        logModel.setCreatedTime(LocalDateTime.now());
        logModel.setToken(encryt(logModel));
        boolean result = rebateLogService.save(logModel);
        recordEarning(user.getId(), user.getFirstLeader(), earnMoney, order);

        if (!user.getSecondLeader().equals(0)) {
            logModel.setId(null);
            logModel.setToken(null);
            logModel.setGoldUserId(user.getSecondLeader());
            logModel.setLevel(2);
            logModel.setRemark("二级返佣结算");
            BigDecimal secondMoney = computeSecondMoney(order.getAllPrice());
            logModel.setMoney(secondMoney);
            logModel.setCreatedTime(LocalDateTime.now());
            logModel.setToken(encryt(logModel));
            boolean secondResult = rebateLogService.save(logModel);
            recordEarning(user.getId(), user.getSecondLeader(), secondMoney, order);
        }
    }

    /**
     * 更新订单状态
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BizException.class})
    public boolean notifyOrderStatus(Integer userId, Order order, GoodsTypeEnum goodsTypeEnum) {
        //更新日志记录状态
        List<CdtRebateLog> listLogModel = rebateLogService.list(new QueryWrapper<CdtRebateLog>()
                .lambda().eq(CdtRebateLog::getBuyUserId, userId).eq(CdtRebateLog::getOrderSn, order.getOrderSn()));
        List<CdtRebateLog> batListLog = new ArrayList<>();
        for (CdtRebateLog item : listLogModel) {
            String token = item.getToken();
            Integer id = item.getId();
            item.setId(null);
            item.setToken(null);
            String dynamicToken = encryt(item);
            if (dynamicToken.equalsIgnoreCase(token)) {
                item.setId(id);
                if (order.getOrderStatus().equals(OrderStatusEnum.COMPLETED_ORDER.getCode())) {
                    item.setCompleteTime(LocalDateTime.now());
                } else {
                    item.setConfirmTime(LocalDateTime.now());
                }

                item.setStatus(order.getOrderStatus());
                item.setToken(encryt(item));
                batListLog.add(item);
            } else {
                log.warn("分销安全校验失败==========》" + JSON.toJSONString(item));
                throw new BizException(BizErrorCodeEnum.SAFE_EXCEPTION);
            }
        }
        if (batListLog.size() > 0) {
            boolean result = rebateLogService.updateBatchById(batListLog);
        }

        //更新用户所得的钱记录状态
        List<CdtDistridetail> distridetail = distridetailService.list(new QueryWrapper<CdtDistridetail>().lambda()
                .eq(CdtDistridetail::getUserId, userId).eq(CdtDistridetail::getOrderSn, order.getOrderSn())
                .ne(CdtDistridetail::getStatus, DistributionStatus.COMPLETED_ORDER.getCode())
        );

        List<CdtDistridetail> batListDetail = new ArrayList<>();
        for (CdtDistridetail detail : batListDetail) {
            String token = detail.getToken();
            Long id = detail.getId();
            detail.setId(null);
            detail.setToken(null);
            String dynamicToken = encryt(detail);
            if (dynamicToken.equalsIgnoreCase(token)) {
                detail.setId(id);
                detail.setUpdateTime(new Date());
                changeDistridetailStatus(order, detail);
                detail.setToken(encryt(detail));
                batListDetail.add(detail);
            } else {
                log.warn("分销安全校验失败==========》" + JSON.toJSONString(detail));
                throw new BizException(BizErrorCodeEnum.SAFE_EXCEPTION);
            }
        }
        if (batListDetail.size() > 0) {
            boolean result = distridetailService.updateBatchById(batListDetail);

        }

        return true;
    }

    /**
     * 更新用户分销参数信息-下一版本迭代
     */
    public void updateUserConfigInfo() {

    }


    /**
     * 分佣计算返佣金+合伙人计算分成
     */
    public void computeCommission() {

    }

    /**
     * 判定是否能成功合伙人
     */
    public void determinePartner() {


    }
}
