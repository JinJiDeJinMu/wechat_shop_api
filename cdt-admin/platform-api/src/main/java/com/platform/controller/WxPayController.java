package com.platform.controller;

import com.alibaba.fastjson.JSON;
import com.chundengtai.base.constant.CacheConstant;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import com.chundengtai.base.weixinapi.ShippingTypeEnum;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.CdtPaytransRecordEntity;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.entity.UserVo;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.util.wechat.WechatUtil;
import com.platform.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Api(tags = "商户支付")
@RestController
@Slf4j
@RequestMapping("/apis/v2/wxpay")
public class WxPayController extends ApiBaseAction {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ApiOrderService orderService;
    @Autowired
    private ApiOrderGoodsService orderGoodsService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private UserRecordSer userRecordSer;
    @Autowired
    private MlsUserSer mlsUserSer;
    @Autowired
    private ApiGoodsService apiGoodsService;
    @Autowired
    private GroupBuyingService groupBuyingService;
    @Autowired
    private GroupBuyingDetailedService groupBuyingDetailedService;
    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private CdtPaytransRecordService paytransRecordService;

    /**
     * 获取支付的请求参数
     */
    @ApiOperation(value = "获取支付的请求参数")
    @GetMapping("prepay")
    public Object getPrepayInfo(@LoginUser UserVo loginUser, Integer orderId, String appId) {
        String reqId = UUID.randomUUID().toString();
        OrderVo orderInfo = orderService.queryObject(orderId);
        if (null == orderInfo) {
            return toResponsObject(400, "订单已经取消", "");
        }

        if (orderInfo.getPay_status().equals(PayTypeEnum.PAYED.getCode())) {
            return toResponsObject(400, "订单已支付，请不要重复操作", "");
        }
        Map orderGoodsParam = new HashMap();
        orderGoodsParam.put("order_id", orderId);
        List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);

        String detail = "未名严选商城-";
        if (null != orderGoods) {
            for (OrderGoodsVo goodsVo : orderGoods) {
                detail = detail + goodsVo.getGoods_name() + "-" + goodsVo.getGoods_specifition_name_value() + goodsVo.getNumber() + "件,";
            }
            if (detail.length() > 0) {
                detail = detail.substring(0, detail.length() - 1);
            }
        }
        WxPayUnifiedOrderRequest payRequest = WxPayUnifiedOrderRequest.newBuilder()
                .body("未名严选校园电商-支付").detail(detail)
                .totalFee(orderInfo.getActual_price().multiply(new BigDecimal(100)).intValue())
                .spbillCreateIp(getClientIp())
                .notifyUrl(ResourceUtil.getConfigByName("wx.notifyUrl"))
                .tradeType(WxPayConstants.TradeType.JSAPI)
                .openid(loginUser.getWeixin_openid())
                .outTradeNo(orderInfo.getOrder_sn())
                .build();
        payRequest.setSignType(WxPayConstants.SignType.MD5);

        WxPayUnifiedOrderResult wxPayUnifiedOrderResult = null;
        try {
            wxPayUnifiedOrderResult = this.wxPayService.unifiedOrder(payRequest);
            log.info(wxPayUnifiedOrderResult.toString());
        } catch (WxPayException e) {
            e.printStackTrace();
            return toResponsFail("下单失败,网关参数异常,error=" + e.getMessage());
        }
        Map<Object, Object> resultObj = new TreeMap();
        try {
            String wxResutlJson = JSON.toJSONString(wxPayUnifiedOrderResult);
            if (wxPayUnifiedOrderResult.getReturnCode().equalsIgnoreCase("FAIL")) {
                log.info(reqId + "=====>微信支付请求失败返回1:" + System.lineSeparator() + wxResutlJson);
                return toResponsFail("支付失败," + wxPayUnifiedOrderResult.getReturnMsg());
            } else if (wxPayUnifiedOrderResult.getReturnCode().equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = wxPayUnifiedOrderResult.getReturnCode();
                String err_code_des = wxPayUnifiedOrderResult.getErrCodeDes();
                if (result_code.equalsIgnoreCase("FAIL")) {
                    log.info(reqId + "=====>微信支付请求失败返回2:" + System.lineSeparator() + wxResutlJson);
                    return toResponsFail("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    log.info(reqId + "=====>微信支付请求成功返回:" + System.lineSeparator() + wxResutlJson);
                    String prepayId = wxPayUnifiedOrderResult.getPrepayId();
                    resultObj.put("appId", wxPayUnifiedOrderResult.getAppid());
                    resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("nonceStr", wxPayUnifiedOrderResult.getNonceStr());
                    resultObj.put("package", "prepay_id=" + prepayId);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, ResourceUtil.getConfigByName("wx.paySignKey"));
                    resultObj.put("paySign", paySign);
                    // 业务处理
                    orderInfo.setPay_id(prepayId);
                    // 付款中
                    orderInfo.setPay_status(PayTypeEnum.PAYING.getCode());
                    orderService.update(orderInfo);

                    //redis设置订单状态
                    redisTemplate.opsForValue().set(orderInfo.getOrder_sn(), "51", 2, TimeUnit.DAYS);
                    try {
                        //FIXME:支付成功后入账
                        CdtPaytransRecordEntity payrecord = new CdtPaytransRecordEntity();
                        payrecord.setMchOrderNo(orderInfo.getOrder_sn());
                        payrecord.setOrderNo(orderInfo.getOrder_sn());
                        payrecord.setCreator(loginUser.getUserId().intValue());
                        payrecord.setCreatorName(loginUser.getNickname());

                        payrecord.setMchId(wxPayUnifiedOrderResult.getMchId());

                        payrecord.setAmount(orderInfo.getActual_price());
                        payrecord.setTradePrice(orderInfo.getActual_price());
                        payrecord.setAppId(appId);
                        payrecord.setCreateDate(new Date());
                        //payrecord.setPayOrderId(prepayId);
                        payrecord.setPayType(PayTypeEnum.PAYING.getCode());
                        payrecord.setMemo(detail);
                        payrecord.setPayState(PayTypeEnum.PAYING.getCode());
                        payrecord.setReqText(payRequest.toXML());
                        //payrecord.setResText(wxResutlJson);
                        payrecord.setResText(wxPayUnifiedOrderResult.getXmlString());
                        paytransRecordService.save(payrecord);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return toResponsObject(0, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(reqId + "=====>微信支付请求异常返回:" + e.getMessage());
            return toResponsFail("下单失败,error=" + e.getMessage());
        }
        return toResponsFail("下单失败");
    }

    /**
     * 获取支付的请求参数
     */
    @ApiOperation(value = "获取支付的请求参数")
    @GetMapping("prepay1")
    public Object payPrepay(@LoginUser UserVo loginUser, Integer orderId) {
        String allOrderId = orderService.queryObject(orderId).getAll_order_id();
        List<OrderVo> orders = orderService.queryByAllOrderId(allOrderId);

        String body = null;
        BigDecimal allPrice = BigDecimal.ZERO;
        for (OrderVo o : orders) {
            if (null == o) {
                return toResponsObject(400, "订单已取消", "");
            }

            if (o.getPay_status().compareTo(1) > 0) {
                return toResponsObject(400, "订单已支付，请不要重复操作", "");
            }

            //计算总价格
            allPrice = allPrice.add(o.getActual_price());

            if (!(body == null)) {
                continue;
            }
            Map orderGoodsParam = new HashMap();
            orderGoodsParam.put("order_id", o.getId());
            //订单的商品
            List<OrderGoodsVo> orderGoods = orderGoodsService.queryList(orderGoodsParam);
            if (null != orderGoods) {
                for (OrderGoodsVo goodsVo : orderGoods) {
                    if (body == null) {
                        body = goodsVo.getGoods_name();
                    } else {
                        body = body + "等";
                        break;
                    }
                }

            }
        }

        String nonceStr = CharUtil.getRandomString(32);
        Map<Object, Object> resultObj = new TreeMap();
        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
            // 商家账号。
            parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            parame.put("out_trade_no", allOrderId);
            // 商品描述
            parame.put("body", body);
            //支付金额
            parame.put("total_fee", allPrice.multiply(new BigDecimal(100)).intValue());
            System.out.println("***************" + parame.get("total_fee") + "***************");
            //System.out.println(orderInfo.getActual_price().multiply(new BigDecimal(100)).intValue() + "***************");
            //parame.put("total_fee", 1);
            // 回调地址
            parame.put("notify_url", ResourceUtil.getConfigByName("wx.notifyUrl"));
            // 交易类型APP
            parame.put("trade_type", ResourceUtil.getConfigByName("wx.tradeType"));
            parame.put("spbill_create_ip", getClientIp());
            parame.put("openid", loginUser.getWeixin_openid());
            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
            // 数字签证
            parame.put("sign", sign);

            String xml = MapUtils.convertMap2Xml(parame);
            System.out.println("***************xml=" + xml + "***************");
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.uniformorder"), xml));
            System.out.println("================resultUn=" + resultUn + "================");

            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            if (return_code.equalsIgnoreCase("FAIL")) {
                return toResponsFail("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    return toResponsFail("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", ResourceUtil.getConfigByName("wx.appId"));
                    //resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("timeStamp", System.currentTimeMillis() / 1000 + "");
                    resultObj.put("nonceStr", nonceStr);
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, ResourceUtil.getConfigByName("wx.paySignKey"));
                    resultObj.put("paySign", paySign);

                    OrderVo newOrder = new OrderVo();
                    newOrder.setPay_id(prepay_id);
                    newOrder.setPay_status(PayTypeEnum.PAYING.getCode());
                    newOrder.setAll_order_id(allOrderId.toString());
                    orderService.updateStatus(newOrder);

                    //redis设置订单状态
                    redisTemplate.opsForValue().set(allOrderId.toString(), "51", 2, TimeUnit.DAYS);
                    return toResponsObject(0, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("下单失败,error=" + e.getMessage());
        }
        return toResponsFail("下单失败");
    }

    /**
     * 微信查询订单状态
     */
    @ApiOperation(value = "查询订单状态")
    @GetMapping("query")
    public Object orderQuery(@LoginUser UserVo loginUser, Integer orderId) {
        if (orderId == null) {
            return toResponsFail("订单不存在");
        }
        OrderVo order = orderService.queryObject(orderId);
        //处理订单的redis状态
        String value = stringRedisTemplate.opsForValue().get(order.getOrder_sn());
        if (value != null && "51".equals(value)) {
            stringRedisTemplate.delete(orderId.toString());
        } else {
            //异步回调已结操作过
            return toResponsMsgSuccess("已完成");
        }

        if (order.getPay_status().intValue() == 2) {
            return toResponsMsgSuccess("支付成功");
        }
        Map<Object, Object> parame = new TreeMap<Object, Object>();
        parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
        // 商家账号。
        parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
        String randomStr = CharUtil.getRandomNum(18).toUpperCase();
        // 随机字符串
        parame.put("nonce_str", randomStr);
        // 商户订单编号
        parame.put("out_trade_no", order.getOrder_sn());
        String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
        log.info("xml:" + xml);
        Map<String, Object> resultUn = null;
        try {
            resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.orderquery"), xml));
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("查询失败,error=" + e.getMessage());
        }
        // 响应报文
        String return_code = MapUtils.getString("return_code", resultUn);
        String return_msg = MapUtils.getString("return_msg", resultUn);

        if (!"SUCCESS".equals(return_code)) {
            return toResponsFail("查询失败,error=" + return_msg);
        }

        String trade_state = MapUtils.getString("trade_state", resultUn);

        if ("SUCCESS".equals(trade_state)) {
            // 更改订单状态
            // 业务处理
//            OrderVo orderInfo = new OrderVo();
//            orderInfo.setId(orderId);
//            orderInfo.setPay_status(2);
//            orderInfo.setOrder_status(201);
//            orderInfo.setShipping_status(0);
//            orderInfo.setPay_time(new Date());
            orderSetPayedStatus(order);
            orderService.update(order);
            return toResponsMsgSuccess("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
            String queryRepeatNum = CacheConstant.SHOP_CACHE_NAME + "queryRepeatNum" + order.getAll_order_id();
            Integer num = (Integer) redisTemplate.opsForValue().get(queryRepeatNum);
            if (num == null) {
                redisTemplate.opsForValue().increment(queryRepeatNum, 1);
                redisTemplate.expire(queryRepeatNum, 2, TimeUnit.DAYS);
                this.orderQuery(loginUser, orderId);
            } else if (num <= 3) {
                redisTemplate.delete(queryRepeatNum);
                this.orderQuery(loginUser, orderId);
            } else {
                return toResponsFail("查询失败,error=" + trade_state);
            }

        } else {
            return toResponsFail("查询失败,error=" + trade_state);
        }

        return toResponsFail("查询失败，未知错误");
    }

    /**
     * 微信订单回调接口
     *
     * @return
     */
    @ApiOperation(value = "微信订单回调接口")
    @RequestMapping(value = "/notify")
    @IgnoreAuth
    @ResponseBody
    public String notify(HttpServletRequest request, HttpServletResponse response) throws
            IOException, WxPayException {
        String reqId = UUID.randomUUID().toString();
        log.info(reqId + "====notify==>返回给微信回调处理结果====>");
        String resultWxMsg = "";//返回给微信的处理结果
        String inputLine;
        String reponseXml = "";
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        //微信给返回的东西
        try {
            while ((inputLine = request.getReader().readLine()) != null) {
                reponseXml += inputLine;
            }
            request.getReader().close();
        } catch (Exception e) {
            e.printStackTrace();
            resultWxMsg = setXml("fail", "xml获取失败");
        }

        if (StringUtils.isNullOrEmpty(reponseXml)) {
            resultWxMsg = setXml("fail", "xml为空");
        }

        if (!StringUtils.isNullOrEmpty(resultWxMsg)) {
            return resultWxMsg;
        }

        log.info(reqId + "===notify==微信回调返回======================>" + reponseXml);
        WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(reponseXml);
        String result_code = wxPayOrderNotifyResult.getResultCode();
        if (result_code.equalsIgnoreCase("FAIL")) {
            //订单编号
            String out_trade_no = wxPayOrderNotifyResult.getOutTradeNo();
            log.error(reqId + "===notify==订单" + out_trade_no + "支付失败");
            return setXml("fail", "fail");
        } else if (result_code.equalsIgnoreCase("SUCCESS")) {
            //订单编号
            String out_trade_no = wxPayOrderNotifyResult.getOutTradeNo();
            log.info(reqId + "===notify==订单" + out_trade_no + "支付成功");
            try {
                // 更改订单状态
                // 业务处理
                orderStatusLogic(out_trade_no, wxPayOrderNotifyResult);
            } catch (Exception ex) {
                log.error("=====weixin===notify===error", ex);
                return setXml("fail", "fail");
            }
            return setXml("SUCCESS", "OK");
        }
        return setXml("SUCCESS", "OK");
    }

    private void orderStatusLogic(String out_trade_no, WxPayOrderNotifyResult result) {
        OrderVo orderItem = orderService.queryOrderNo(out_trade_no);
        if (orderItem == null) {
            orderItem = orderService.queryByOrderId(out_trade_no);

            orderItem.setAll_order_id(out_trade_no);
        } else {
            orderItem.setOrder_sn(out_trade_no);
        }
        log.info("微信回调设置=====out_trade_no>:" + out_trade_no);
        log.info("微信回调Json=====>:" + JSON.toJSONString(orderItem));

        orderSetPayedStatus(orderItem);
        orderItem.setPay_time(new Date());
        int rows = orderService.update(orderItem);
        log.warn("=====此次更新影响===行数====" + rows);
        try {
            CdtPaytransRecordEntity payrecord = new CdtPaytransRecordEntity();
            payrecord.setMchOrderNo(orderItem.getOrder_sn());
            payrecord.setAmount(BigDecimal.valueOf(result.getTotalFee()));
            payrecord.setTradePrice(BigDecimal.valueOf(result.getTotalFee()));
            payrecord.setAppId("");
            payrecord.setCreateDate(new Date());
            //payrecord.setPayOrderId(result.getTransactionId());
            payrecord.setPayType(PayTypeEnum.PAYED.getCode());
            payrecord.setMemo(result.getPromotionDetail());
            payrecord.setPayState(PayTypeEnum.PAYED.getCode());
            payrecord.setResText(result.getXmlString());
            paytransRecordService.save(payrecord);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void orderSetPayedStatus(OrderVo orderItem) {
        orderItem.setPay_status(PayTypeEnum.PAYED.getCode());
        if (orderItem.getGoods_type().equals(GoodsTypeEnum.WRITEOFF_ORDER.getCode())
        ) {
            orderItem.setOrder_status(OrderStatusEnum.NOT_USED.getCode());
        } else if (orderItem.getGoods_type().equals(GoodsTypeEnum.EXPRESS_GET.getCode())) {
            orderItem.setOrder_status(OrderStatusEnum.WAIT_SHIPPED.getCode());
        } else {
            orderItem.setOrder_status(OrderStatusEnum.PAYED_ORDER.getCode());
        }
        orderItem.setShipping_status(ShippingTypeEnum.NOSENDGOODS.getCode());
    }


//    /**
//     * 订单退款请求（暂无使用）
//     */
//    @ApiOperation(value = "订单退款请求")
//    @PostMapping("refund")
//    public Object refund(Integer orderId) {
//        OrderVo orderInfo = orderService.queryObject(orderId);
//
//        if (null == orderInfo) {
//            return toResponsObject(400, "订单已取消", "");
//        }
//
//        if (orderInfo.getOrder_status() == 401 || orderInfo.getOrder_status() == 402) {
//            return toResponsObject(400, "订单已退款", "");
//        }
//
//        if (orderInfo.getPay_status() != 2) {
//            return toResponsObject(400, "订单未付款，不能退款", "");
//        }
//
//		WechatRefundApiResult result = WechatUtil.wxRefund(orderInfo.getAll_order_id().toString(),
//				orderInfo.getAll_price().doubleValue(), orderInfo.getAll_price().doubleValue());
//        if (result.getResult_code().equals("SUCCESS")) {
//            if (orderInfo.getOrder_status() == 201) {
//                orderInfo.setOrder_status(401);
//            } else if (orderInfo.getOrder_status() == 300) {
//                orderInfo.setOrder_status(402);
//            }
//            
//            //修改订单状态
//            OrderVo neworderInfo = new OrderVo();
//            neworderInfo.setAll_order_id(orderInfo.getAll_order_id());
//            neworderInfo.setOrder_status(orderInfo.getOrder_status());
//            orderService.updateStatus(orderInfo);
//            
//            //还原优惠券使用状态
//			UserCouponVo uc = new UserCouponVo();
//			uc.setId(orderInfo.getCoupon_id());
//			uc.setCoupon_status(1);
//			uc.setUsed_time(null);
//			userCouponService.updateCouponStatus(uc);
//            
//            return toResponsObject(400, "成功退款", "");
//        } else {
//            return toResponsObject(400, "退款失败", "");
//        }
//    }


    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    //模拟微信回调接口
    public static String callbakcXml(String orderNum) {
        return "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[" + orderNum + "]]></out_trade_no>  <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
    }

}