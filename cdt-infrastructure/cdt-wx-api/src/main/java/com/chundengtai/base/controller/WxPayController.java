package com.chundengtai.base.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.bean.CdtScoreFlow;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.constant.CacheConstant;
import com.chundengtai.base.entity.CdtPaytransRecordEntity;
import com.chundengtai.base.entity.OrderGoodsVo;
import com.chundengtai.base.entity.OrderVo;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.facade.IdistributionFacade;
import com.chundengtai.base.jwt.JavaWebToken;
import com.chundengtai.base.service.*;
import com.chundengtai.base.util.ApiBaseAction;
import com.chundengtai.base.util.wechat.WechatUtil;
import com.chundengtai.base.utils.*;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Api(tags = "v2商户支付")
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
    private WxPayService wxPayService;

    @Autowired
    private CdtPaytransRecordService paytransRecordService;

    @Autowired
    private OrderService cdtOrderService;

    @Autowired
    private IdistributionFacade distributionFacade;

    @Autowired
    private CdtScoreFlowService cdtScoreFlowService;

    @Autowired
    private CdtScoreService cdtScoreService;

    @Autowired
    private UserScoreService userScoreService;

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
                String goodsSpecifitionNameValue = goodsVo.getGoods_specifition_name_value();
                if (org.springframework.util.StringUtils.isEmpty(goodsSpecifitionNameValue))
                    goodsSpecifitionNameValue = "";
                detail = detail + goodsVo.getGoods_name() + "-" + goodsSpecifitionNameValue + goodsVo.getNumber() + "件,";
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
                    redisTemplate.opsForValue().set(orderInfo.getOrder_sn(), "51", 1, TimeUnit.DAYS);
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

    @ApiOperation(value = "积分支付")
    @GetMapping("payscore")
    @IgnoreAuth
    public Object payScore(@LoginUser UserVo loginUser, @RequestParam("scoreflowId") String scoreflowId) {
        String reqId = UUID.randomUUID().toString();

        CdtScoreFlow cdtScoreFlow = cdtScoreFlowService.getById(scoreflowId);
        if (cdtScoreFlow == null) {
            return toResponsObject(400, "积分购买订单不存在", "");
        }
        if (cdtScoreFlow.getPayStatus().equals(PayTypeEnum.PAYED.getCode())) {
            return toResponsObject(400, "积分购买订单已支付，请不要重复操作", "");
        }
        String describe = cdtScoreService.getById(cdtScoreFlow.getScoreId()).getDetail();
        String token = cdtScoreFlow.getToken();

        cdtScoreFlow.setToken(null);
        cdtScoreFlow.setPayStatus(null);
        cdtScoreFlow.setCreateTime(null);
        cdtScoreFlow.setId(null);
        cdtScoreFlow.setPayTime(null);
        String token_flag = gettoken(cdtScoreFlow);
        if (token_flag.equalsIgnoreCase(token)) {
            log.info("=====token校验成功");
            WxPayUnifiedOrderRequest payRequest = WxPayUnifiedOrderRequest.newBuilder()
                    .body("未名严选校园电商-支付").detail(describe)
                    .totalFee(cdtScoreFlow.getMoney().multiply(new BigDecimal(100)).intValue())
                    .spbillCreateIp(getClientIp())
                    .notifyUrl(ResourceUtil.getConfigByName("wx.scorenotifyUrl"))
                    .tradeType(WxPayConstants.TradeType.JSAPI)
                    .openid(loginUser.getWeixin_openid())
                    .outTradeNo(cdtScoreFlow.getFlowSn())
                    .build();
            payRequest.setSignType(WxPayConstants.SignType.MD5);
            System.out.println(payRequest);
            cdtScoreFlow = cdtScoreFlowService.getById(scoreflowId);
            WxPayUnifiedOrderResult wxPayUnifiedOrderResult = null;
            try {
                wxPayUnifiedOrderResult = wxPayService.unifiedOrder(payRequest);
                log.info(wxPayUnifiedOrderResult.toString());
                System.out.println(wxPayUnifiedOrderResult.toString());
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
                        // 付款中
                        cdtScoreFlow.setPayStatus(PayTypeEnum.PAYING.getCode());
                        cdtScoreFlowService.updateById(cdtScoreFlow);

                        //redis设置订单状态
                        redisTemplate.opsForValue().set(cdtScoreFlow.getFlowSn(), "51", 1, TimeUnit.DAYS);
                        return toResponsObject(0, "微信统一订单下单成功", resultObj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(reqId + "=====>微信支付请求异常返回:" + e.getMessage());
                return toResponsFail("下单失败,error=" + e.getMessage());
            }

        }
        return toResponsObject(400, "下单失败", "");

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
        Order order = cdtOrderService.getById(orderId);
        //处理订单的redis状态
        String value = stringRedisTemplate.opsForValue().get(order.getOrderSn());
        if (value != null && "51".equals(value)) {
            stringRedisTemplate.delete(orderId.toString());
        } else {
            //异步回调已结操作过
            return toResponsMsgSuccess("已完成");
        }

        if (order.getPayStatus() == 2) {
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
        parame.put("out_trade_no", order.getOrderSn());
        String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
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
            orderSetPayedStatus(order);
            cdtOrderService.updateById(order);
            return toResponsMsgSuccess("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
            String queryRepeatNum = CacheConstant.SHOP_CACHE_NAME + "queryRepeatNum" + order.getAllOrderId();
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

    @ApiOperation(value = "微信积分订单查询接口")
    @GetMapping("/queryscore")
    @IgnoreAuth
    @ResponseBody
    public Object queryScore(@LoginUser UserVo loginUser, Integer scoreflowId) {

        if (scoreflowId == null) {
            return toResponsFail("订单不存在");
        }
        CdtScoreFlow cdtScoreFlow = cdtScoreFlowService.getById(scoreflowId);
        if (cdtScoreFlow == null) {
            return toResponsFail("订单不存在");
        }
        //处理订单的redis状态
        String value = stringRedisTemplate.opsForValue().get(cdtScoreFlow.getFlowSn());
        if (value != null && "51".equals(value)) {
            stringRedisTemplate.delete(cdtScoreFlow.getFlowSn());
        } else {
            //异步回调已结操作过
            return toResponsMsgSuccess("已完成");
        }

        if (cdtScoreFlow.getPayStatus() == 2) {
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
        parame.put("out_trade_no", cdtScoreFlow.getFlowSn());
        String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
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

            return toResponsMsgSuccess("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {

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
        String resultWxMsg = "";
        String inputLine;
        String reponseXml = "";
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
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


    /**
     * 微信积分订单回调接口
     *
     * @return
     */
    @ApiOperation(value = "微信积分订单回调接口")
    @RequestMapping(value = "/scorenotify")
    @IgnoreAuth
    @ResponseBody
    public String scorenotify(HttpServletRequest request, HttpServletResponse response) throws
            IOException, WxPayException {
        String reqId = UUID.randomUUID().toString();
        log.info(reqId + "====scorenotify==>返回给微信回调处理结果====>");
        String resultWxMsg = "";
        String inputLine;
        String reponseXml = "";
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
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

        log.info(reqId + "===scorenotify==微信回调返回======================>" + reponseXml);
        WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(reponseXml);
        String result_code = wxPayOrderNotifyResult.getResultCode();
        if (result_code.equalsIgnoreCase("FAIL")) {
            //订单编号
            String out_trade_no = wxPayOrderNotifyResult.getOutTradeNo();
            log.error(reqId + "===scorenotify==订单" + out_trade_no + "支付失败");
            return setXml("fail", "fail");
        } else if (result_code.equalsIgnoreCase("SUCCESS")) {
            //订单编号
            String out_trade_no = wxPayOrderNotifyResult.getOutTradeNo();
            log.info(reqId + "===scorenotify==订单" + out_trade_no + "支付成功");
            try {
                // 更改订单状态
                // 业务处理
                userScoreService.addUserScore(out_trade_no);
            } catch (Exception ex) {
                log.error("=====weixin===scorenotify===error", ex);
                return setXml("fail", "fail");
            }
            return setXml("SUCCESS", "OK");
        }
        return setXml("SUCCESS", "OK");
    }


    private void orderStatusLogic(String out_trade_no, WxPayOrderNotifyResult result) {
        Order orderItem = cdtOrderService.getOne(new QueryWrapper<Order>().lambda().eq(Order::getOrderSn, out_trade_no));
        if (orderItem == null) {
            orderItem = cdtOrderService.getOne(new QueryWrapper<Order>().lambda().eq(Order::getAllOrderId, out_trade_no));

            orderItem.setAllOrderId(out_trade_no);
        } else {
            orderItem.setOrderSn(out_trade_no);
        }
        log.info("微信回调设置=====out_trade_no>:" + out_trade_no);
        log.info("微信回调Json=====>:" + JSON.toJSONString(orderItem));

        orderSetPayedStatus(orderItem);
        orderItem.setPayTime(new Date());
        boolean rows = cdtOrderService.updateById(orderItem);
        log.warn("=====此次更新影响===行数====" + rows);
        try {
            if (rows) {
                distributionFacade.recordDistributeLog(orderItem.getUserId(), orderItem);
                //distributionFacade.notifyOrderStatus(orderItem.getUserId(), orderItem, GoodsTypeEnum.getEnumByKey(orderItem.getGoodsType()));
                CdtPaytransRecordEntity payrecord = new CdtPaytransRecordEntity();
                payrecord.setMchOrderNo(orderItem.getOrderSn());
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void orderSetPayedStatus(Order orderItem) {
        orderItem.setPayStatus(PayTypeEnum.PAYED.getCode());
        if (orderItem.getGoodsType().equals(GoodsTypeEnum.WRITEOFF_ORDER.getCode())
        ) {
            orderItem.setOrderStatus(OrderStatusEnum.NOT_USED.getCode());
        } else if (orderItem.getGoodsType().equals(GoodsTypeEnum.EXPRESS_GET.getCode())) {
            orderItem.setOrderStatus(OrderStatusEnum.WAIT_SHIPPED.getCode());
        } else {
            orderItem.setOrderStatus(OrderStatusEnum.PAYED_ORDER.getCode());
        }
        orderItem.setShippingStatus(ShippingTypeEnum.NOSENDGOODS.getCode());
    }


    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    //模拟微信回调接口
    public static String callbakcXml(String orderNum) {
        return "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[" + orderNum + "]]></out_trade_no>  <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
    }

    public String gettoken(Object object) {
        Map chain = null;
        try {
            chain = BeanJwtUtil.javabean2map(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = JavaWebToken.createJavaWebToken(chain);
        return token;
    }

}