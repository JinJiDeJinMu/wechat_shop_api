package com.chundengtai.base.listener;

import com.alibaba.fastjson.JSON;
import com.chundengtai.base.event.DistributionEvent;
import com.chundengtai.base.service.DistributionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("UserBindListener")
@Slf4j
public class UserBindListener {
    @Autowired
    private DistributionService distributionService;

    @EventListener(DistributionEvent.class)
    @Order(0)
    public void defaultShopCartEvent(DistributionEvent event) {
        log.info("----------用户绑定时间出发---------" + JSON.toJSONString(event));
        try {
            asyncExcute(event);
        } catch (Exception ex) {
            log.error("绑定分销用户关系异常=======》" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Async
    public void asyncExcute(DistributionEvent event) {
        log.info(" testAsync 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());
        distributionService.distributionLogic(event);
    }
}
