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

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
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
    @Scheduled(cron = "0 0/20 * * * ?")
    public void orderCancel() {
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getPayStatus, PayTypeEnum.NOPAY.getCode())
                .eq(Order::getOrderStatus, OrderStatusEnum.WAIT_PAY.getCode()));
        if (orderList != null) {
            orderList.forEach(e -> {
                long num = 60;
                long minutes = Duration.between(LocalDateTime.now(), DateTimeConvert.date2LocalDateTime(e.getAddTime())).toMinutes();
                if (minutes > num) {
                    e.setOrderStatus(OrderStatusEnum.CANCEL_ORDER.getCode());
                    orderService.updateById(e);
                }
            });
        }
    }

    /**
     * 确认收货超过7天订单已自动完成
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void orderFinish() {
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.CONFIRM_GOODS.getCode())
                .eq(Order::getPayStatus, PayTypeEnum.PAYED.getCode()));
        log.info("====确认收货====" + orderList);
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

    }

    public static void main(String[] args) throws ParseException {
        LocalDateTime now = LocalDateTime.of(2020, 1, 25, 9, 43, 20);
        LocalDateTime now1 = LocalDateTime.of(2020, 2, 25, 9, 53, 20);
        Duration day = Duration.between(now, now1);
        System.out.println(day.toDays());
        System.out.println(Period.between(now.toLocalDate(), now1.toLocalDate()).getDays());
        System.out.println(day.toMinutes());
    }
}
