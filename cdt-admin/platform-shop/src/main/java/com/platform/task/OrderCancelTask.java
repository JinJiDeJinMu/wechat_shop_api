package com.platform.task;

import com.chundengtai.base.bean.Order;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
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

    @Scheduled(cron = "0 0/20 * * * ? ")
    public void orderCancel() {

        HashMap<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("order_status", OrderStatusEnum.WAIT_PAY.getCode());
        List<Order> orderList = orderService.queryList(hashMap);

        if (orderList != null) {
            orderList.forEach(order -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(order.getAddTime());
                cal.add(Calendar.MINUTE, 60);
                if (new Date().after(cal.getTime())) {
                    order.setOrderStatus(OrderStatusEnum.CANCEL_ORDER.getCode());
                    orderService.updateById(order);
                }
            });
        }
    }
}
