package com.platform.controller;

import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.OrderType;
import com.platform.entity.OrderVo;
import com.platform.service.ApiOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description 订单定时取消任务
 * @Author hujinbiao
 * @Date 2019年12月20日 0020 下午 02:29:12
 * @Version 1.0
 **/
@Component
public class OrderCancelJob {

    @Autowired
    private ApiOrderService apiOrderService;

    @Scheduled(cron = "0 0/30 * * * ? ")
    public void orderCancel() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("payStatus", OrderType.NORMAL.getCode());
        List<OrderVo> orderEntityList = apiOrderService.queryList(hashMap);
        orderEntityList.forEach(orderVo -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(orderVo.getAdd_time());
            cal.add(Calendar.MINUTE, 30);
            Date d2 = cal.getTime();
            if (new Date().after(d2)) {
                orderVo.setOrder_status(OrderStatusEnum.CANCEL_ORDER.getCode());
                apiOrderService.update(orderVo);
            }
        });
    }

    @Scheduled(cron = "0/2 * * * * ？")
    public void test() {
        System.out.println("-------------------");
    }

    public static void main(String[] args) {
        String[] str = {"111", "222"};
        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);
        }

    }

}
