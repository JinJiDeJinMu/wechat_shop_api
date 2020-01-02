package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chundengtai.base.bean.*;
import com.chundengtai.base.constant.CacheConstant;
import com.chundengtai.base.event.DistributionEvent;
import com.chundengtai.base.exception.BizException;
import com.chundengtai.base.jwt.JavaWebToken;
import com.chundengtai.base.service.*;
import com.chundengtai.base.utils.BeanJwtUtil;
import com.chundengtai.base.utils.DateTimeConvert;
import com.chundengtai.base.weixinapi.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 分销系统应用服务
 */
@Service
@Slf4j
public class DistributionService implements IdistributionService {

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

    @Autowired
    private CdtUserSummaryService cdtUserSummaryService;

    @Autowired
    private PartnerService partnerService;

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
            int daysNum = Period.between(LocalDateTime.now().toLocalDate(), DateTimeConvert.date2LocalDateTime(order.getConfirmTime()).toLocalDate()).getDays();
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
                    .lambda().eq(CdtDistrimoney::getStatus, OnOffEnum.ON.getCode()).orderByDesc(CdtDistrimoney::getId));
            if (model != null) {
                redisTemplate.opsForValue().set(CacheConstant.SERVICE_CDT_DISTRIMONEY, model, 30, TimeUnit.DAYS);
            }
        }
        return model;
    }

    private BigDecimal computeFirstMoney(BigDecimal totalMoney) {
        return totalMoney.multiply(distrimoney.getFirstPercent());
    }

    private BigDecimal computeSecondMoney(BigDecimal totalMoney) {
        return totalMoney.multiply(distrimoney.getSecondPercent());
    }
    //返佣比例配置结束

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void distributionLogic(DistributionEvent event) {
        log.info(" distributionLogic 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());
        //todo:对推荐信息解密
        if (StringUtils.isEmpty(event.getEncryptCode())) {
            return;
        }

        if (event.getEncryptCode().contains("_")) {
            String promotionId = event.getEncryptCode().split("_")[0];
            Long parentId = Long.valueOf(promotionId);
            log.warn("推荐人id====>" + parentId);
            System.out.printf("推荐人id====>" + parentId);
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
            model.setUserId(event.getUserId().intValue());
            model.setParentId(parentId.intValue());
            if (parentModel != null) {
                model.setSponsorId(parentModel.getSponsorId());
            } else {
                model.setSponsorId(parentId.intValue());
            }

            //绑定user表层架关系
            boolean resultRows = userService.update(new UpdateWrapper<User>().lambda().set(User::getIsDistribut, TrueOrFalseEnum.TRUE.getCode())
                    .eq(User::getId, parentId).eq(User::getIsDistribut, TrueOrFalseEnum.FALSE.getCode())
            );
            log.warn("====开通推荐的分销合伙人按钮====>" + resultRows);

            User userInfo = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getId, event.getUserId()));
            log.warn("用户id====>" + event.getUserId());
            if (userInfo.getSecondLeader().equals(0)) {
                LambdaUpdateWrapper<User> condition = new LambdaUpdateWrapper<>();
                if (userInfo.getFirstLeader().equals(0)) {
                    condition.set(User::getFirstLeader, parentId);
                    try {
                        //绑定链路关系 //记录标号信息
                        bindLinkRelation(event, parentId);
                    } catch (Exception ex) {
                        log.error("绑定链路关系异常");
                        ex.printStackTrace();
                    }
                }

                condition.set(User::getIsDistribut, TrueOrFalseEnum.TRUE.getCode());
                condition.eq(User::getId, event.getUserId());
                if (parentModel != null) {
                    model.setSponsorId(parentModel.getSponsorId());
                    condition.set(User::getSecondLeader, parentModel.getParentId());
                }
                //更新用户表一二级合伙人绑定
                boolean rows = userService.update(condition);
                log.info("====绑定影响行数====" + rows);
            }
            boolean result = distributionLevelService.save(model);
        }
    }

    private void bindLinkRelation(DistributionEvent event, Long parentId) {
        int count = cdtUserSummaryService.count(new QueryWrapper<CdtUserSummary>().lambda().eq(CdtUserSummary::getUserId, event.getUserId().intValue()));
        if (count > 0) {
            return;
        }
        CdtUserSummary cdtUserSummary = cdtUserSummaryService.getOne(new QueryWrapper<CdtUserSummary>().lambda().eq(CdtUserSummary::getUserId, parentId.intValue()));
        Gson gson = new Gson();
        CdtUserSummary userSummary = new CdtUserSummary();
        userSummary.setUserId(event.getUserId().intValue());

        // 绑定用户链路关系的同时更新上级发展的下线数量
        if (cdtUserSummary == null) {
            cdtUserSummary = new CdtUserSummary();
            cdtUserSummary.setUserId(parentId.intValue());
            LinkedList<Integer> chain = new LinkedList<>();
            chain.add(parentId.intValue());
            cdtUserSummary.setChainRoad(gson.toJson(chain));
            cdtUserSummary.setStatsPerson(1);
            boolean result = cdtUserSummaryService.save(cdtUserSummary);
            chain.add(event.getUserId().intValue());
            userSummary.setChainRoad(gson.toJson(chain));
        } else if (cdtUserSummary.getChainRoad() != null) {
            Type linkNodeType = new TypeToken<LinkedList<Integer>>() {
            }.getType();
            LinkedList<Integer> linkNode = gson.fromJson(cdtUserSummary.getChainRoad(), linkNodeType);
            linkNode.add(event.getUserId().intValue());
            userSummary.setChainRoad(gson.toJson(linkNode));
            cdtUserSummary.setStatsPerson(cdtUserSummary.getStatsPerson() + 1);
            boolean result2 = cdtUserSummaryService.updateById(cdtUserSummary);
        }
        boolean rows = cdtUserSummaryService.save(userSummary);
    }

    /**
     * 绑定用户关系
     *
     * @param userId         the user id
     * @param referrerEncpyt the referrer encpyt
     */
    @Override
    public void referreRelation(long userId, String referrerEncpyt) {
        DistributionEvent event = new DistributionEvent();
        event.setEncryptCode(referrerEncpyt);
        event.setUserId(userId);
        eventPublisher.publishEvent(event);
    }

    /**
     * 记录用户所挣钱
     */
    public boolean recordEarning(int userId, int goldUserId, BigDecimal money, Order order, int type) {
        CdtDistridetail distridetail = new CdtDistridetail();
        distridetail.setUserId(userId);
        distridetail.setGoldUserId(goldUserId);
        distridetail.setMoney(money);
        distridetail.setOrderSn(order.getOrderSn());
        distridetail.setCreatedTime(new Date());
        changeDistridetailStatus(order, distridetail);
        distridetail.setType(type);
        distridetail.setToken(encryt(distridetail));
        boolean result = distridetailService.save(distridetail);
        return result;
        //更新用户分销参数信息
    }

    /**
     * 记录分销日志
     */
    @Override
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
        logModel.setToken(encryt(logModel));
        logModel.setCreatedTime(new Date());
        boolean result = rebateLogService.save(logModel);

        //记录分销奖励
        recordEarning(user.getId(), user.getFirstLeader(), earnMoney, order, 0);

        //记录二级分销奖励
        if (!user.getSecondLeader().equals(0)) {
            logModel.setId(null);
            logModel.setToken(null);
            logModel.setGoldUserId(user.getSecondLeader());
            logModel.setLevel(2);
            logModel.setRemark("二级返佣结算");
            BigDecimal secondMoney = computeSecondMoney(order.getAllPrice());
            logModel.setMoney(secondMoney);
            logModel.setToken(encryt(logModel));
            logModel.setCreatedTime(new Date());
            boolean secondResult = rebateLogService.save(logModel);
            recordEarning(user.getId(), user.getSecondLeader(), secondMoney, order, 0);
        }

        try {
            //记录合伙人奖励
            CdtUserSummary partner = partnerService.getPartnerInfo(distrimoney, user.getId());
            if (partner != null) {
                BigDecimal percent = BigDecimal.ZERO;
                switch (partner.getPartnerLevel()) {
                    case 2:
                        percent = distrimoney.getSecondPartner();
                        break;
                    case 3:
                        percent = distrimoney.getThirdPartner();
                        break;
                    default:
                    case 1:
                        percent = distrimoney.getFirstPartner();
                        break;
                }
                BigDecimal partnerMoney = order.getAllPrice().multiply(percent);
                recordEarning(user.getId(), partner.getUserId(), partnerMoney, order, 1);
            }
        } catch (Exception ex) {
            log.error("========记录合伙人奖励异常===============");
            ex.printStackTrace();
        }
    }

    /**
     * 更新订单状态
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BizException.class})
    public boolean notifyOrderStatus(Integer userId, Order order, GoodsTypeEnum goodsTypeEnum) {
        //更新日志记录状态
        List<CdtRebateLog> listLogModel = rebateLogService.list(new QueryWrapper<CdtRebateLog>()
                .lambda().eq(CdtRebateLog::getBuyUserId, userId).eq(CdtRebateLog::getOrderSn, order.getOrderSn()));
        List<CdtRebateLog> batListLog = new ArrayList<>();
        for (CdtRebateLog item : listLogModel) {
            String token = item.getToken();
            Integer id = item.getId();
            Date createTime = item.getCreatedTime();
            Date completeTime = item.getCompleteTime();
            Date confirmTime = item.getConfirmTime();
            item.setId(null);
            item.setToken(null);
            item.setCreatedTime(null);
            item.setCompleteTime(null);
            item.setConfirmTime(null);
            log.info("实体类=" + item);
            try {
                log.info("map=" + BeanJwtUtil.javabean2map(item));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String dynamicToken = encryt(item);
            log.info("解密1=" + JavaWebToken.parserJavaWebToken(token));
            log.info("解密2=" + JavaWebToken.parserJavaWebToken(dynamicToken));
            log.info("token=" + token);
            log.info("dynamicToken=" + dynamicToken);
            //if (dynamicToken.equalsIgnoreCase(token)) {
            item.setId(id);

            item.setStatus(order.getOrderStatus());
            item.setToken(encryt(item));
            item.setCreatedTime(createTime);
            item.setCompleteTime(completeTime);
            item.setConfirmTime(confirmTime);
            if (order.getOrderStatus().equals(OrderStatusEnum.COMPLETED_ORDER.getCode())) {
                item.setCompleteTime(new Date());
            } else {
                item.setConfirmTime(new Date());
            }

            batListLog.add(item);
//            } else {
//                log.warn("分销安全校验失败==========》" + JSON.toJSONString(item));
//                throw new BizException(BizErrorCodeEnum.SAFE_EXCEPTION);
//            }
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

        BiConsumer<CdtDistridetail, Order> userSumeryOp = (CdtDistridetail detailModel, Order orderModel) -> {
            CdtDistributionLevel item = distributionLevelService.getOne(new QueryWrapper<CdtDistributionLevel>().lambda()
                    .eq(CdtDistributionLevel::getUserId, orderModel.getUserId()));

            log.info("============BiConsumer====================>进入表达式逻辑");
            if (item == null) {
                log.error("==userSumeryOp====绑定关系层级不存在不存在！=====");
                return;
            }
            //更新用户统计有效下线
            CdtUserSummary partner = cdtUserSummaryService.getOne(new QueryWrapper<CdtUserSummary>().lambda()
                    .eq(CdtUserSummary::getUserId, item.getParentId()));
            if (partner == null) {
                log.error("==userSumeryOp====推荐人不存在！=====");
            }

            if (order.getOrderStatus().equals(OrderStatusEnum.REFUND_ORDER.getCode())) {
                if (item.getTradeOrderNum() == 1) {
                    //绑定用户层级关系编号
                    item.setDevNum(partner.getTradePerson());
                    item.setIsTrade(TrueOrFalseEnum.FALSE.getCode());
                }

                assert partner != null;
                partner.setUnbalanced(partner.getUnbalanced() == null ? BigDecimal.ZERO : partner.getUnbalanced().subtract(detailModel.getMoney()));
                partner.setStatsPerson((partner.getTradePerson() == null || partner.getTradePerson() == 0) ? 0 : partner.getTradePerson() - 1);
                partner.setInvalidOrderNum((partner.getInvalidOrderNum() == null || partner.getInvalidOrderNum() == 0) ? 0 : partner.getInvalidOrderNum() + 1);
                partner.setRefundOrderNum((partner.getRefundOrderNum() == null || partner.getRefundOrderNum() == 0) ? 0 : partner.getRefundOrderNum() + 1);
                item.setTradeOrderNum((item.getTradeOrderNum() == null || item.getTradeOrderNum() == 0) ? 0 : item.getTradeOrderNum() - 1);
            } else {
                assert partner != null;
                partner.setStatsPerson((partner.getTradePerson() == null || partner.getTradePerson() == 0) ? 1 : partner.getTradePerson() + 1);
                partner.setUnbalanced(partner.getUnbalanced() == null ? detailModel.getMoney() : partner.getUnbalanced().add(detailModel.getMoney()));

                //绑定用户层级关系编号
                item.setDevNum(partner.getTradePerson());
                item.setIsTrade(TrueOrFalseEnum.TRUE.getCode());
                item.setTradeOrderNum(item.getTradeOrderNum() == null ? 1 : item.getTradeOrderNum() + 1);
            }
            boolean rows = cdtUserSummaryService.updateById(partner);
            log.warn("==userSumeryOp====更新用户统计有效下线=====>" + rows);

            boolean rows2 = distributionLevelService.updateById(item);
            log.warn("==userSumeryOp====绑定用户层级关系编号=====>" + rows2);
        };

        for (CdtDistridetail detail : distridetail) {
            String token = detail.getToken();
            Long id = detail.getId();
            Date creatTime = detail.getCreatedTime();
            Date updateTime = detail.getUpdateTime();
            detail.setId(null);
            detail.setToken(null);
            detail.setCreatedTime(null);
            detail.setUpdateTime(null);
            String dynamicToken = encryt(detail);
            //if (dynamicToken.equalsIgnoreCase(token)) {
            detail.setId(id);
            changeDistridetailStatusLogic(order, detail, userSumeryOp);
            detail.setToken(encryt(detail));

            //时间放在加密之后
            detail.setUpdateTime(new Date());
            detail.setCreatedTime(creatTime);
            batListDetail.add(detail);
//            } else {
//                log.warn("分销安全校验失败==========》" + JSON.toJSONString(detail));
//                throw new BizException(BizErrorCodeEnum.SAFE_EXCEPTION);
//            }
        }
        if (batListDetail.size() > 0) {
            boolean result = distridetailService.updateBatchById(batListDetail);

        }

        return true;
    }

    private void changeDistridetailStatusLogic(Order order, CdtDistridetail distridetail, BiConsumer<CdtDistridetail, Order> userSumeryOp) {
        if (!order.getOrderStatus().equals(OrderStatusEnum.COMPLETED_ORDER.getCode())) {
            distridetail.setStatus(DistributionStatus.NON_COMPLETE_ORDER.getCode());
        } else if (order.getOrderStatus().equals(OrderStatusEnum.COMPLETED_ORDER.getCode()) &&
                order.getGoodsType().equals(GoodsTypeEnum.ORDINARY_GOODS.getCode())
        ) {
            int daysNum = Period.between(LocalDateTime.now().toLocalDate(), DateTimeConvert.date2LocalDateTime(order.getConfirmTime()).toLocalDate()).getDays();
            if (daysNum < 7) {
                distridetail.setStatus(DistributionStatus.NOT_SERVEN_ORDER.getCode());
            } else {
                distridetail.setStatus(DistributionStatus.COMPLETED_ORDER.getCode());
                userSumeryOp.accept(distridetail, order);
            }
        } else if (order.getOrderStatus().equals(OrderStatusEnum.COMPLETED_ORDER.getCode()) &&
                (order.getGoodsType().equals(GoodsTypeEnum.WRITEOFF_ORDER.getCode()) ||
                        order.getGoodsType().equals(GoodsTypeEnum.EXPRESS_GET.getCode()
                        ))) {
            distridetail.setStatus(DistributionStatus.COMPLETED_ORDER.getCode());
            userSumeryOp.accept(distridetail, order);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.REFUND_ORDER.getCode())) {
            distridetail.setStatus(DistributionStatus.REFUND_ORDER.getCode());
            userSumeryOp.accept(distridetail, order);
        }
    }

}
