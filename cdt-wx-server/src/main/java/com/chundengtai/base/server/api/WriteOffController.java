package com.chundengtai.base.server.api;


import com.chundengtai.base.server.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单核销功能<br>
 */
@Api(tags = "订单核销功能")
@RestController
@RequestMapping("/api/v2/writeoff")
@Slf4j
public class WriteOffController extends ApiBaseAction {

    @ApiOperation(value = "获得核销码基础信息", httpMethod = "POST")
    @RequestMapping("/getWriteOffCodeInfo")
    @ResponseBody
    public Object getWriteOffCodeInfo(@ApiParam(name = "orderNo", value = "订单号") @RequestParam String orderNo) {

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
