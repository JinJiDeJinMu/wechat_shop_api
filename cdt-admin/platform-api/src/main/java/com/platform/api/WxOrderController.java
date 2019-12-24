package com.platform.api;

import com.alibaba.fastjson.JSON;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import com.chundengtai.base.weixinapi.ShippingTypeEnum;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.CdtPaytransRecordEntity;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.entity.UserVo;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.utils.CharUtil;
import com.platform.utils.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "v2订单相关")
@RestController
@RequestMapping("/apis/v2/order")
@Slf4j
public class WxOrderController extends ApiBaseAction {
    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private OrderService cdtOrderService;

    @Autowired
    private ApiOrderService orderService;

    @Autowired
    private ApiOrderGoodsService orderGoodsService;

    @Autowired
    private UserRecordSer userRecordSer;

    @Autowired
    private ApiUserCouponService userCouponService;

    @Autowired
    private KeygenService kengenService;

    @Autowired
    private CdtPaytransRecordService paytransRecordService;

    /**
     * 获取订单列表(要验证)
     */
    @ApiOperation(value = "获取订单列表")
    @RequestMapping("list")
    public Object list(@LoginUser UserVo loginUser, Integer order_status,
                       Integer merchantId,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map params = new HashMap();
        params.put("user_id", loginUser.getUserId());
        if (merchantId != null && !merchantId.equals(0)) {
            params.put("merchantId", merchantId);
        }
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "desc");
        params.put("order_status", order_status);
        //查询列表数据
        Query query = new Query(params);
        PageHelper.startPage(page, size);
        List<OrderVo> orderEntityList = orderService.queryPageList(query);
        PageInfo pageInfo = new PageInfo(orderEntityList);
        return toResponsSuccess(pageInfo);
    }

    /**
     * 获取订单列表(要验证)
     */
    @ApiOperation(value = "获取订单列表")
    @RequestMapping("merchantlist")
    public Object merchantlist(@LoginUser UserVo loginUser, Integer order_status,
                               Integer merchantId,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (merchantId != null && !merchantId.equals(0)) {
            if (!merchantId.equals(loginUser.getMerchant_id().intValue())) {
                toResponsObject(3, "您不是对应的店铺管理员,无权查看", null);
            }
            Map params = new HashMap();
            params.put("merchantId", merchantId);
            params.put("page", page);
            params.put("limit", size);
            params.put("sidx", "id");
            params.put("order", "desc");
            params.put("order_status", order_status);
            //查询列表数据
            Query query = new Query(params);
            PageHelper.startPage(page, size);
            List<OrderVo> orderEntityList = orderService.queryPageList(query);
            PageInfo pageInfo = new PageInfo(orderEntityList);
            return toResponsSuccess(pageInfo);
        }
        return toResponsFail("无数据显示");
    }

    /**
     * 获取订单详情
     */
    @ApiOperation(value = "获取订单详情")
    @IgnoreAuth
    @GetMapping("detail")
    public Object detail(Integer orderId) {
        Map resultObj = new HashMap();
        OrderVo orderInfo = orderService.queryObject(orderId);
        if (null == orderInfo) {
            return toResponsObject(400, "订单不存在", "");
        }

        Map orderGoodsParam = new HashMap();
        orderGoodsParam.put("order_id", orderId);
        //订单的商品
        List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);
        //订单最后支付时间
        if (orderInfo.getOrder_status() == 0) {
            // if (moment().subtract(60, 'minutes') < moment(orderInfo.add_time)) {
//            orderInfo.final_pay_time = moment("001234", "Hmmss").format("mm:ss")
            // } else {
            //     //超过时间不支付，更新订单状态为取消
            // }
        }

        //订单可操作的选择,删除，支付，收货，评论，退换货
        Map handleOption = orderInfo.getHandleOption();
        resultObj.put("orderInfo", orderInfo);
        resultObj.put("orderGoods", orderGoods);
        resultObj.put("handleOption", handleOption);
        if (!StringUtils.isEmpty(orderInfo.getShipping_code()) && !StringUtils.isEmpty(orderInfo.getShipping_no())) {
            resultObj.put("shippingList", null);
        }
        return toResponsSuccess(resultObj);
    }

    /**
     * 修改订单
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = "修改订单")
    @PostMapping("updateSuccess")
    public Object updateSuccess(Integer orderId) {
        OrderVo orderInfo = orderService.queryObject(orderId);
        if (orderInfo == null) {
            return toResponsFail("订单不存在");
        } else if (orderInfo.getOrder_status() != 0) {
            return toResponsFail("订单状态不正确orderStatus" + orderInfo.getOrder_status() + "payStatus" + orderInfo.getPay_status());
        }

        orderInfo.setId(orderId);
        orderInfo.setPay_status(PayTypeEnum.PAYED.getCode());
        orderInfo.setOrder_status(OrderStatusEnum.PAYED_ORDER.getCode());
        orderInfo.setShipping_status(ShippingTypeEnum.NOSENDGOODS.getCode());
        orderInfo.setPay_time(new Date());
        int num = orderService.update(orderInfo);
        if (num > 0) {
            return toResponsMsgSuccess("修改订单成功");
        } else {
            return toResponsFail("修改订单失败");
        }
    }

    /**
     * 订单提交
     */
    @ApiOperation(value = "订单提交")
    @PostMapping("submit")
    public Object submit(@LoginUser UserVo loginUser) {
        Map resultObj = null;
        try {
            resultObj = orderService.submit(getJsonRequest(), loginUser);
            if (null != resultObj) {
                return toResponsObject(MapUtils.getInteger(resultObj, "errno"), MapUtils.getString(resultObj, "errmsg"), resultObj.get("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("提交失败");
    }

    /**
     * 获取订单列表
     */
    @ApiOperation(value = "取消订单")
    @RequestMapping("cancelOrder")
    public Object cancelOrder(Integer orderId) {
        OrderVo orderVo = orderService.queryObject(orderId);
        List<OrderVo> orders = orderService.queryByAllOrderId(orderVo.getAll_order_id());
        BigDecimal allPrice = BigDecimal.ZERO;
        for (OrderVo o : orders) {
            allPrice = allPrice.add(o.getAll_price());
        }

        if (orderVo.getOrder_status().equals(OrderStatusEnum.SHIPPED_ORDER.getCode())) {
            return toResponsFail("已发货，不能取消");
        } else if (orderVo.getOrder_status().equals(OrderStatusEnum.CONFIRM_GOODS.getCode())) {
            return toResponsFail("已收货，不能取消");
        }
        try {
            return returnMoney(orderVo, allPrice, orderVo.getOrder_sn());
        } catch (WxPayException e) {
            logger.error(e.getXmlString());
            returnAllMoney(orderVo, e);
            if (e.getErrCodeDes().contains("订单不存在")) {
                try {
                    return returnMoney(orderVo, allPrice, orderVo.getAll_order_id());
                } catch (WxPayException ex) {
                    returnAllMoney(orderVo, e);
                }
            }
            return toResponsSuccess(e.getErrCodeDes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsSuccess("提交失败");
    }

    private void returnAllMoney(OrderVo orderVo, WxPayException e) {
        if (e.getErrCodeDes().contains("订单已全额退款")) {
            orderVo.setOrder_status(OrderStatusEnum.REFUND_ORDER.getCode());
            orderVo.setPay_status(PayTypeEnum.REFUND.getCode());
            orderService.update(orderVo);

            try {
                //FIXME:支付成功后入账
                CdtPaytransRecordEntity payrecord = new CdtPaytransRecordEntity();
                payrecord.setMchOrderNo(orderVo.getOrder_sn());
                payrecord.setOrderNo(orderVo.getOrder_sn());
                payrecord.setAmount(orderVo.getActual_price());
                payrecord.setTradePrice(orderVo.getActual_price());
                payrecord.setAppId("");
                payrecord.setCreateDate(new Date());
                //payrecord.setPayOrderId("");
                payrecord.setPayType(PayTypeEnum.REFUND.getCode());
                payrecord.setMemo("订单已全额退款");
                payrecord.setPayState(PayTypeEnum.REFUND.getCode());
                paytransRecordService.save(payrecord);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private Object returnMoney(OrderVo orderVo, BigDecimal allPrice, String outTradeNo) throws WxPayException {
        // 需要退款
        if (orderVo.getPay_status().equals(PayTypeEnum.PAYED.getCode())) {
            WxPayRefundRequest request = new WxPayRefundRequest();
            request.setNonceStr(CharUtil.getRandomString(16));
            request.setOutTradeNo(outTradeNo);
            request.setRefundFee(allPrice.multiply(new BigDecimal(100)).intValue());
            request.setTotalFee(allPrice.multiply(new BigDecimal(100)).intValue());

            long id = kengenService.genNumber().longValue();
            request.setOutRefundNo(String.valueOf(id));
            request.setOpUserId(orderVo.getUser_id().toString());
            WxPayRefundResult wxResult = wxPayService.refund(request);

            if (wxResult.getResultCode().equals("SUCCESS")) {
                if (orderVo.getOrder_status().equals(OrderStatusEnum.PAYED_ORDER.getCode())) {
                    orderVo.setOrder_status(OrderStatusEnum.REFUND_ORDER.getCode());
                } else if (orderVo.getOrder_status().equals(OrderStatusEnum.SHIPPED_ORDER.getCode())) {
                    orderVo.setOrder_status(OrderStatusEnum.COMPLETED_ORDER.getCode());
                } else {
                    orderVo.setOrder_status(OrderStatusEnum.REFUND_ORDER.getCode());
                }
                orderVo.setPay_status(PayTypeEnum.REFUND.getCode());
                logger.warn("=====退款回调成功=====" + JSON.toJSONString(wxResult));
                orderService.update(orderVo);

                try {
                    CdtPaytransRecordEntity payrecord = new CdtPaytransRecordEntity();
                    payrecord.setMchOrderNo(orderVo.getOrder_sn());
                    payrecord.setAmount(BigDecimal.valueOf(request.getTotalFee()));
                    payrecord.setTradePrice(BigDecimal.valueOf(request.getRefundFee()));
                    payrecord.setAppId("");
                    payrecord.setCreateDate(new Date());
                    //payrecord.setPayOrderId(wxResult.getTransactionId());
                    payrecord.setPayType(PayTypeEnum.REFUND.getCode());
                    payrecord.setMemo(wxResult.getXmlString());
                    payrecord.setPayState(PayTypeEnum.REFUND.getCode());
                    payrecord.setReqText(request.toXML());
                    payrecord.setResText(wxResult.getXmlString());
                    paytransRecordService.save(payrecord);
                } catch (Exception ex) {
                    log.error("订单退款异常=====>" + ex.getMessage());
                    ex.printStackTrace();
                }

                return toResponsSuccess("取消成功");
            } else {
                return toResponsObject(400, "取消成失败", "取消成失败");
            }
        } else {
            orderVo.setOrder_status(OrderStatusEnum.CANCEL_ORDER.getCode());
            orderService.update(orderVo);
            return toResponsSuccess("取消成功");
        }
    }

    /**
     * 确认收货
     */
    @ApiOperation(value = "确认收货")
    @RequestMapping("confirmOrder")
    public Object confirmOrder(Integer orderId) {
        try {
            Order orderVo = cdtOrderService.getById(orderId);

            if (orderVo.getOrderStatus().equals(OrderStatusEnum.SHIPPED_ORDER.getCode()) ||
                    orderVo.getOrderStatus().equals(OrderStatusEnum.WAIT_SHIPPED.getCode())
            ) {
                orderVo.setOrderStatus(OrderStatusEnum.COMPLETED_ORDER.getCode());
                orderVo.setShippingStatus(ShippingTypeEnum.GETEDGOODS.getCode());
                orderVo.setConfirmTime(LocalDateTime.now());
                cdtOrderService.updateById(orderVo);
                return toResponsSuccess("确认收货成功");
            } else {
                return toResponsSuccess("订单状态不对,请核实后在操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toResponsFail("操作失败,请联系客服");
    }
}