package com.chundengtai.base.controller;

import com.chundengtai.base.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 订单核销功能<br>
 */
@Api(value = "订单核销", tags = "订单核销功能")
@RestController
@RequestMapping("/api/v2/product")
@Slf4j
public class ProductV2Controller extends ApiBaseAction {

    @ApiOperation(value = "获得核销码基础信息", httpMethod = "POST")
    @GetMapping("/getbuyusers")
    @ResponseBody
    public Object getWriteOffCodeInfo(@ApiParam(name = "goodid", value = "产品号") @RequestParam Integer goodid) {

        return toResponsSuccess("发送成功");
    }

    @ApiOperation(value = "二维码核销", httpMethod = "POST")
    @RequestMapping("/writeOffCodeExecute")
    @ResponseBody
    public Object writeOffCodeExecute(
            @ApiParam(name = "orderNo", value = "订单号") @RequestParam String orderNo,
            @ApiParam(name = "timestamp", value = "时间戳") @RequestParam String timestamp,
            @ApiParam(name = "tokenSgin", value = "token秘钥") @RequestParam String tokenSgin
    ) {

        return toResponsSuccess("发送成功");
    }

}
