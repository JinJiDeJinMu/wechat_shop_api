package com.platform.api;

import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.OrderVo;
import com.platform.service.ApiOrderService;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单核销功能<br>
 */
@Api(value = "订单核销", tags = "订单核销功能")
@RestController
@RequestMapping("/api/v2/writeoff")
@Slf4j
public class WriteOffController extends ApiBaseAction {
    @Autowired
    private ApiOrderService orderService;

    @ApiOperation(value = "获得核销码基础信息", httpMethod = "POST")
    @RequestMapping("/getWriteOffCodeInfo")
    @ResponseBody
    public Object getWriteOffCodeInfo(@ApiParam(name = "orderNo", value = "订单号") @RequestParam String orderNo) {

        return toResponsSuccess("发送成功");
    }

    @ApiOperation(value = "二维码核销", httpMethod = "POST")
    @RequestMapping("/writeOffCodeExecute")
    @ResponseBody
    @IgnoreAuth
    public Object writeOffCodeExecute(
            @ApiParam(name = "orderNo", value = "订单号")
            @RequestParam String orderNo,
            @RequestParam Integer orderId,
            @RequestParam Integer userId,
            Integer merchatnId,
            @ApiParam(name = "timestamp", value = "时间戳") String timestamp,
            @ApiParam(name = "tokenSgin", value = "token秘钥") String tokenSgin
    ) {

        //todo:核销订单验证参数加密
        //todo:核销订单验证权限
        //todo:核销订单更改状态
        OrderVo orderVo = new OrderVo();
        orderVo.setId(orderId);
        orderVo.setOrder_sn(orderNo);
        orderVo.setOrder_status(OrderStatusEnum.COMPLETED_ORDER.getCode());
        orderVo.setOrder_status_text(OrderStatusEnum.COMPLETED_ORDER.getDesc());
        int rows = orderService.update(orderVo);

        log.info("核销====》" + orderNo);
        if (rows > 0) {
            return toResponsSuccess("核销成功");
        }
        return toResponsSuccess("核销失败");
    }

}
