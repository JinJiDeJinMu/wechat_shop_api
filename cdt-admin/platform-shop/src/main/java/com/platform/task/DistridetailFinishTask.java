package com.platform.task;

import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


   /* @Scheduled(cron = "0 0 0 * * ？")
    public void change() {

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

    }*/
}
