package com.chundengtai.base.application;

import com.chundengtai.base.event.DistributionEvent;
import com.chundengtai.base.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 分销系统应用服务
 */
@Service
public class DistributionService {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CdtDistrimoneyService distrimoneyService;

    @Autowired
    private CdtDistridetailService distridetailService;

    @Autowired
    private CdtRebateLogService rebateLogService;

    @Autowired
    private CdtUserDistributionService userDistributionService;

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
}
