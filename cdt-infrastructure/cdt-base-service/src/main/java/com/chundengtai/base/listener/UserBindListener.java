package com.chundengtai.base.listener;

import com.alibaba.fastjson.JSON;
import com.chundengtai.base.event.DistributionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("UserBindListener")
@Slf4j
public class UserBindListener {
    @EventListener(DistributionEvent.class)
    @Order(0)
    public void defaultShopCartEvent(DistributionEvent event) {

        log.info("----------用户绑定时间出发---------" + JSON.toJSONString(event));
    }
}
