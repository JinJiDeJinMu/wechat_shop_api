package com.platform.task;

import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.OrderType;
import com.platform.entity.OrderEntity;
import com.platform.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description 订单超时未支付定时取消任务
 * @Author hujinbiao
 * @Date 2019年12月23日 0023 上午 10:53:55
 * @Version 1.0
 **/
@Component
public class OrderCancelTask {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 0/30 * * * ? ")
    public void orderCancel() {

        HashMap<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("payStatus", OrderType.NORMAL.getCode());
        List<OrderEntity> orderEntityList = orderService.queryList(hashMap);
        orderEntityList.forEach(orderEntity -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(orderEntity.getAddTime());
            cal.add(Calendar.MINUTE, 30);
            if (new Date().after(cal.getTime())) {
                orderEntity.setOrderStatus(OrderStatusEnum.CANCEL_ORDER.getCode());
                orderService.update(orderEntity);
            }
        });
    }
}
