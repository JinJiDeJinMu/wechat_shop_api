package com.platform.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.service.impl.DistributionService;
import com.chundengtai.base.utils.DateTimeConvert;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @Description 订单相关任务
 * @Author hujinbiao
 * @Date 2019年12月23日 0023 上午 10:53:55
 * @Version 1.0
 **/
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DistributionService distributionService;

    /**
     * 订单未支付超过60分钟取消
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void orderCancel() {
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getPayStatus, PayTypeEnum.NOPAY.getCode())
                .eq(Order::getOrderStatus, OrderStatusEnum.WAIT_PAY.getCode()));
        if (orderList != null) {
            orderList.forEach(e -> {
                long num = 60;
                long minutes = Duration.between(DateTimeConvert.date2LocalDateTime(e.getAddTime()), LocalDateTime.now()).toMinutes();
                if (minutes > num) {
                    e.setOrderStatus(OrderStatusEnum.CANCEL_ORDER.getCode());
                    orderService.updateById(e);
                }
            });
        }
    }

    /*   */

    /**
     * 确认收货超过7天订单已自动完成
     *//*
    @Scheduled(cron = "0 0/50 * * * ?")
    public void orderFinish() {
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.CONFIRM_GOODS.getCode())
                .eq(Order::getPayStatus, PayTypeEnum.PAYED.getCode()));
        if (orderList != null) {
            orderList.forEach(e -> {
                long num = 7;
                long daysNum = Duration.between(DateTimeConvert.date2LocalDateTime(e.getConfirmTime()), LocalDateTime.now()).toDays();
                if (daysNum > num) {
                    e.setOrderStatus(OrderStatusEnum.COMPLETED_ORDER.getCode());
                    boolean flag = orderService.updateById(e);
                    //通知分销订单状态改变
                    if (flag) {
                        distributionService.notifyOrderStatus(e.getUserId(), e, GoodsTypeEnum.getEnumByKey(e.getGoodsType()));
                    }
                }
            });
        }

    }*/
    @Scheduled(cron = "0 0/10 * * * ?")
    public void orderChange() {
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.COMPLETED_ORDER.getCode())
                .eq(Order::getPayStatus, PayTypeEnum.PAYED.getCode())
                .eq(Order::getGoodsType, GoodsTypeEnum.ORDINARY_GOODS.getCode()));
        if (orderList != null) {
            orderList.forEach(e -> {
                long num = 7;
                long daysNum = Duration.between(DateTimeConvert.date2LocalDateTime(e.getConfirmTime()), LocalDateTime.now()).toDays();
                if (daysNum > num) {
                    //通知分销订单状态改变
                    distributionService.notifyOrderStatus(e.getUserId(), e, GoodsTypeEnum.getEnumByKey(e.getGoodsType()));
                }
            });
        }

    }
}
