package com.chundengtai.base.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chundengtai.base.bean.*;
import com.chundengtai.base.event.DistributionEvent;
import com.chundengtai.base.jwt.JavaWebToken;
import com.chundengtai.base.utils.BeanJwtUtil;
import com.chundengtai.base.weixinapi.OnOffEnum;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.TrueOrFalseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 分销系统应用服务
 */
@Service
@Slf4j
public class DistributionService {

    public static final String SERVICE_CDT_DISTRIMONEY = "DistributionService:CdtDistrimoney";

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

    private void encryt(CdtRebateLog logModel) {
        try {
            Map chain = BeanJwtUtil.javabean2map(logModel);
            logModel.setToken(JavaWebToken.createJavaWebToken(chain));
        } catch (Exception e) {
            log.error("jwt加密异常========");
            e.printStackTrace();
        }
    }

    //
    //返佣比例配置信息获取
    public CdtDistrimoney getDistrimoney() {
        CdtDistrimoney model = (CdtDistrimoney) redisTemplate.opsForValue().get(SERVICE_CDT_DISTRIMONEY);
        if (model == null) {
            model = distrimoneyService.getOne(new QueryWrapper<CdtDistrimoney>()
                    .lambda().eq(CdtDistrimoney::getStatus, OnOffEnum.OFF.getCode()).orderByDesc(CdtDistrimoney::getId));
            if (model != null) {
                redisTemplate.opsForValue().set(SERVICE_CDT_DISTRIMONEY, model, 30, TimeUnit.DAYS);
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
    public void recordEarning(int userId, int goldUserId, BigDecimal money, String orderSn) {
        CdtDistridetail distridetail = new CdtDistridetail();
        distridetail.setUserId(userId);
        distridetail.setGoldUserId(goldUserId);
        distridetail.setMoney(money);
        distridetail.setOrderSn(orderSn);
        distridetail.setCreatedTime(LocalDateTime.now());
        distridetail.setToken("");

        boolean result = distridetailService.save(distridetail);
    }

    /**
     * 记录分销日志
     */
    @Async
    public void recordDistributeLog(Integer userId, Integer orderId) {
        User user = userService.getById(userId);
        Order order = orderService.getById(orderId);

        CdtRebateLog logModel = new CdtRebateLog();
        logModel.setBuyUserId(user.getId());
        logModel.setOrderId(order.getId());
        logModel.setOrderSn(order.getOrderSn());

        logModel.setGoldUserId(user.getFirstLeader());
        logModel.setGoodsPrice(order.getAllPrice());
        logModel.setMechantId(order.getMerchantId());
        logModel.setNickname(user.getNickname());
        logModel.setStatus(OrderStatusEnum.getEnumByKey(order.getOrderStatus()).getDesc());
        logModel.setLevel(1);
        logModel.setRemark("一级返佣结算");
        logModel.setMoney(computeFirstMoney(order.getAllPrice()));
        logModel.setCreatedTime(LocalDateTime.now());

        encryt(logModel);
        boolean result = rebateLogService.save(logModel);

        if (!user.getSecondLeader().equals(0)) {
            logModel.setId(null);
            logModel.setToken(null);
            logModel.setGoldUserId(user.getSecondLeader());
            logModel.setLevel(2);
            logModel.setMoney(computeSecondMoney(order.getAllPrice()));
            encryt(logModel);
            boolean secondResult = rebateLogService.save(logModel);
        }
    }


    /**
     * 更新用户分销参数信息
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
