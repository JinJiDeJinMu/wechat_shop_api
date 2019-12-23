package com.chundengtai.base.application;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chundengtai.base.bean.CdtDistributionLevel;
import com.chundengtai.base.bean.User;
import com.chundengtai.base.event.DistributionEvent;
import com.chundengtai.base.service.*;
import com.chundengtai.base.weixinapi.TrueOrFalseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 分销系统应用服务
 */
@Service
@Slf4j
public class DistributionService {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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

    public void distributionLogic(DistributionEvent event) {
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
    public void recordEarning() {

    }

    /**
     * 记录分销日志
     */
    public void recordDistributeLog() {

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
