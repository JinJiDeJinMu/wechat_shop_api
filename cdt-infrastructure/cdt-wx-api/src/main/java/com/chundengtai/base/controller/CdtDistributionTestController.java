package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.facade.IdistributionFacade;
import com.chundengtai.base.result.R;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.util.CommonUtil;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @Description TODO
 * @Author hujinbiao
 * @Date 2020年1月13日 0013 上午 09:55:15
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/test")
public class CdtDistributionTestController {

    @Autowired
    private IdistributionFacade idistributionFacade;

    @Autowired
    private OrderService orderService;


    @GetMapping("/referreRelation")
    @ResponseBody
    @IgnoreAuth
    public R referreRelation(long userId, String referrerEncpyt) {

        idistributionFacade.referreRelation(userId, referrerEncpyt);

        return R.ok();
    }

    @GetMapping("/order")
    @ResponseBody
    @IgnoreAuth
    public Order order(Integer userId, Integer goodType) {

        Order order = getOrder(userId, goodType);
        orderService.save(order);
        return order;
    }

    @GetMapping("/pay")
    @ResponseBody
    @IgnoreAuth
    public R payOrder(Integer orderId) {

        Order order = orderService.getById(orderId);
        order.setOrderStatus(OrderStatusEnum.PAYED_ORDER.getCode());
        order.setPayStatus(PayTypeEnum.PAYED.getCode());
        boolean result = orderService.updateById(order);
        if (result) {
            idistributionFacade.recordDistributeLog(order.getUserId(), order);
        }

        return R.ok();
    }

    @GetMapping("/changeorder")
    @ResponseBody
    @IgnoreAuth
    public R changeOrderstatus(Integer orderId) {

        Order order = orderService.getById(orderId);
        order.setOrderStatus(OrderStatusEnum.COMPLETED_ORDER.getCode());
        order.setConfirmTime(new Date());
        Boolean result = orderService.updateById(order);
        if (result) {
            idistributionFacade.notifyOrderStatus(order.getUserId(), order, GoodsTypeEnum.WRITEOFF_ORDER);
        }

        return R.ok();
    }

    public Order getOrder(Integer userId, Integer goodType) {
        Order order = new Order();
        BigDecimal orderTotalPrice = new BigDecimal(5);
        // 总订单编号
        order.setAllOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setOrderSn(CommonUtil.generateOrderNumber());
        order.setUserId(userId);
        order.setOrderType("2");
        // 收货地址和运费
        order.setConsignee(null);
        order.setMobile(null);
        order.setCountry(null);
        order.setProvince(null);
        order.setCity(null);
        order.setDistrict(null);
        order.setAddress(null);
        // 留言
        order.setPostscript(null);
        // 使用的优惠券
        order.setCouponId(1);
        order.setCouponPrice(null);
        // 订单金额
        order.setAddTime(new Date());
        order.setGoodsPrice(orderTotalPrice);
        order.setOrderPrice(orderTotalPrice);
        order.setActualPrice(orderTotalPrice);
        order.setAllPrice(orderTotalPrice);
        order.setFreightPrice(orderTotalPrice);
        order.setGoodsType(goodType);
        // 设置为待付款状态
        order.setOrderStatus(OrderStatusEnum.WAIT_PAY.getCode());
        order.setShippingStatus(0);
        order.setPayStatus(0);
        order.setShippingId(0);
        order.setShippingFee(orderTotalPrice);
        order.setIntegral(0);
        order.setIntegralMoney(new BigDecimal(0));
        order.setMerchantId(16L);
        return order;
    }

}
