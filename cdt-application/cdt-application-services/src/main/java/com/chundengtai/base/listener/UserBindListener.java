package com.chundengtai.base.listener;

import com.alibaba.fastjson.JSON;
import com.chundengtai.base.application.DistributionService;
import com.chundengtai.base.event.DistributionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service("UserBindListener")
@Slf4j
public class UserBindListener {
    @Autowired
    DistributionService distributionService;

    @EventListener(DistributionEvent.class)
    @Order(0)
    public void defaultShopCartEvent(DistributionEvent event) {
        log.info("----------用户绑定时间出发---------" + JSON.toJSONString(event));
        try {
            distributionService.distributionLogic(event);
        } catch (Exception ex) {
            log.error("绑定分销用户关系异常=======》" + ex.getMessage());
            ex.printStackTrace();
        }
    }


}
