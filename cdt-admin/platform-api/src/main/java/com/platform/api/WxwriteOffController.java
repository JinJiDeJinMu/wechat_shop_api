package com.platform.api;

import com.chundengtai.base.bean.Order;
import com.chundengtai.base.constant.CacheConstant;
import com.chundengtai.base.facade.DistributionFacade;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserVo;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 订单核销功能<br>
 */
@Api(value = "订单核销", tags = "订单核销功能")
@RestController
@RequestMapping("/api/v2/writeoff")
@Slf4j
public class WxwriteOffController extends ApiBaseAction {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private OrderService cdtOrderService;

    @Autowired
    private DistributionFacade distributionFacade;

    @ApiOperation(value = "获得核销码基础信息", httpMethod = "POST")
    @RequestMapping("/getWriteOffCodeInfo")
    @ResponseBody
    public Object getWriteOffCodeInfo(@ApiParam(name = "orderNo", value = "订单号") @RequestParam String orderNo) {
        return toResponsSuccess("发送成功");
    }

    @ApiOperation(value = "二维码核销", httpMethod = "POST")
    @PostMapping("/writeOffCodeExecute")
    @ResponseBody
    @IgnoreAuth
    public Object writeOffCodeExecute(@LoginUser UserVo loginUser
            , @ApiParam(name = "orderNo", value = "订单号")
                                          @RequestParam String orderNo,
                                      @RequestParam Integer orderId,
                                      @RequestParam Integer userId,
                                      @RequestParam Long merchantId,
                                      @ApiParam(name = "timestamp", value = "时间戳") String timestamp,
                                      @ApiParam(name = "tokenSgin", value = "token秘钥") String tokenSgin
    ) {

        log.info("merchantId" + merchantId);
        if (!merchantId.equals(loginUser.getMerchant_id())) {
            return toResponsSuccess("您当前不是店铺管理员");
        }
        //todo:核销订单验证参数加密
        //todo:核销订单验证权限
        //todo:核销订单更改状态
        Order orderVo = cdtOrderService.getById(orderId);
        if (orderVo.getOrderStatus().equals(OrderStatusEnum.NOT_USED.getCode()) &&
                orderVo.getPayStatus().equals(PayTypeEnum.PAYED.getCode())
        ) {
            orderVo.setOrderSn(orderNo);
            orderVo.setOrderStatus(OrderStatusEnum.COMPLETED_ORDER.getCode());
            boolean rows = cdtOrderService.updateById(orderVo);
            redisTemplate.opsForValue().set(CacheConstant.ORDER_HEXIAO_CACHE + merchantId + ":" + orderNo + ":" + userId, "true", 180, TimeUnit.MINUTES);
            log.info("核销====》" + orderNo);
            if (rows) {
                distributionFacade.notifyOrderStatus(userId, orderVo, GoodsTypeEnum.getEnumByKey(orderVo.getGoodsType()));
                return toResponsSuccess("核销成功");
            }
        } else {
            return toResponsSuccess("订单状态不对,请联系客服管理员咨询");
        }
        return toResponsSuccess("核销失败");
    }

    @ApiOperation(value = "二维码核销回调", httpMethod = "POST")
    @GetMapping("/writeOffCodeNotify")
    @ResponseBody
    @IgnoreAuth
    public Object writeOffCodeNotify(
            @ApiParam(name = "orderNo", value = "订单号")
            @RequestParam String orderNo,
            @RequestParam Integer orderId,
            @RequestParam Integer userId,
            Integer merchantId,
            @ApiParam(name = "timestamp", value = "时间戳") String timestamp,
            @ApiParam(name = "tokenSgin", value = "token秘钥") String tokenSgin
    ) {
        Object result = redisTemplate.opsForValue().get(CacheConstant.ORDER_HEXIAO_CACHE + merchantId + ":" + orderNo + ":" + userId);
        if (result == null) {
            return toResponsObject(1, "还未核销", false);
        } else if (result.equals("true")) {
            return toResponsObject(0, "核销成功", true);
        }
        return toResponsSuccess("核销成功");

    }

}
