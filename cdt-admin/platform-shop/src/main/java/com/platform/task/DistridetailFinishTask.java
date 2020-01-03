package com.platform.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.utils.DateTimeConvert;
import com.chundengtai.base.weixinapi.DistributionStatus;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author hujinbiao
 * @Date 2019年12月31日 0031 上午 09:46:42
 * @Version 1.0
 **/
@Component
public class DistridetailFinishTask {

    @Autowired
    private CdtDistridetailService cdtDistridetailService;

    @Autowired
    private OrderService orderService;


    @Scheduled(cron = "0 0 0 * * ？")
    public void execute() {

        List<CdtDistridetail> cdtDistridetailList = cdtDistridetailService.list(new LambdaQueryWrapper<CdtDistridetail>()
                .eq(CdtDistridetail::getStatus, DistributionStatus.NOT_SERVEN_ORDER.getCode()));
        cdtDistridetailList.forEach(e -> {
            String ordersn = e.getOrderSn();
            Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                    .eq(Order::getOrderStatus, OrderStatusEnum.COMPLETED_ORDER.getCode())
                    .eq(Order::getOrderSn, ordersn));
            if (order != null) {
                long num = 7;
                long daysNum = Duration.between(LocalDateTime.now(), DateTimeConvert.date2LocalDateTime(order.getConfirmTime())).toDays();
                if (daysNum > num) {
                    e.setStatus(DistributionStatus.COMPLETED_ORDER.getCode());
                    e.setUpdateTime(new Date());
                    cdtDistridetailService.updateById(e);
                }
            }
        });


    }
}
