package com.chundengtai.base.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.entity.OrderGoodsVo;
import com.chundengtai.base.entity.ProductVo;
import com.chundengtai.base.service.ApiOrderGoodsService;
import com.chundengtai.base.service.ApiProductService;
import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.service.impl.DistributionService;
import com.chundengtai.base.utils.DateTimeConvert;
import com.chundengtai.base.weixinapi.DistributionStatus;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    @Autowired
    private CdtDistridetailService cdtDistridetailService;

    @Autowired
    private ApiProductService apiProductService;

    @Autowired
    private ApiOrderGoodsService apiOrderGoodsService;


    /**
     * 订单未支付超过60分钟取消
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void orderCancel() {
        try{ List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                .eq(Order::getPayStatus, PayTypeEnum.NOPAY.getCode())
                .eq(Order::getOrderStatus, OrderStatusEnum.WAIT_PAY.getCode()));
            if (orderList != null) {
                orderList.forEach(e -> {
                    long num = 60;
                    long minutes = Duration.between(DateTimeConvert.date2LocalDateTime(e.getAddTime()), LocalDateTime.now()).toMinutes();
                    if (minutes > num) {
                        e.setOrderStatus(OrderStatusEnum.CANCEL_ORDER.getCode());
                        orderService.updateById(e);
                        //更新库存
                        updatGoodsNumber(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 普通订单完成七天之后自动更新分销状态
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void orderChange() {
        List<CdtDistridetail> cdtDistridetailList = cdtDistridetailService.list(new LambdaQueryWrapper<CdtDistridetail>()
                .eq(CdtDistridetail::getStatus, DistributionStatus.NOT_SERVEN_ORDER.getCode()));
        if (cdtDistridetailList != null) {
            cdtDistridetailList.forEach(e -> {
                Order order = orderService.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderSn, e.getOrderSn()));
                if (order != null && order.getGoodsType().equals(GoodsTypeEnum.ORDINARY_GOODS.getCode())) {
                    long num = 7;
                    long daysNum = Duration.between(DateTimeConvert.date2LocalDateTime(order.getConfirmTime()), LocalDateTime.now()).toDays();
                    if (daysNum > num) {
                        distributionService.notifyOrderStatus(e.getUserId(), order, GoodsTypeEnum.getEnumByKey(order.getGoodsType()));
                    }
                }
            });
        }

    }

    //修改库存
    public void updatGoodsNumber(Order order){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("order_id",order.getId());
        List<OrderGoodsVo> orderGoodsVoList = apiOrderGoodsService.queryList(hashMap);
        orderGoodsVoList.forEach(e ->{
            ProductVo productVo = apiProductService.queryObject(e.getProduct_id());
            productVo.setGoods_number(productVo.getGoods_number() + e.getNumber());
            apiProductService.update(productVo);
        });

    }
}
