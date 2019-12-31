package com.platform.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @Description 订单相关任务
 * @Author hujinbiao
 * @Date 2019年12月23日 0023 上午 10:53:55
 * @Version 1.0
 **/
@Component
public class OrderTask {

    @Autowired
    private OrderService orderService;

    /**
     * 订单未支付超过60分钟取消
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    public void orderCancel() {
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getPayStatus, PayTypeEnum.NOPAY.getCode())
                .eq(Order::getOrderStatus, OrderStatusEnum.WAIT_PAY.getCode()));
        if (orderList != null) {
            orderList.forEach(e -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(e.getAddTime());
                cal.add(Calendar.MINUTE, 60);
                if (new Date().after(cal.getTime())) {
                    e.setOrderStatus(OrderStatusEnum.CANCEL_ORDER.getCode());
                    orderService.updateById(e);
                }
            });
        }
    }

    /**
     * 订单已发货超过12天自动确认收货
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void orderFinish() {
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.SHIPPED_ORDER.getCode())
                .eq(Order::getPayStatus, PayTypeEnum.PAYED.getCode()));
        if (orderList != null) {
            orderList.forEach(e -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(e.getPayTime());
                cal.add(Calendar.DATE, 12);
                if (new Date().after(cal.getTime())) {
                    e.setOrderStatus(OrderStatusEnum.COMPLETED_ORDER.getCode());
                    orderService.updateById(e);
                }
            });
        }

    }

}
