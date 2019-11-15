package com.chundengtai.base.server.api;

import com.chundengtai.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 窝边生活<br>
 */
@Api(tags = "窝边生活首页")
@RestController
@RequestMapping("/api/v2/index")
@Slf4j
public class IndexV2Controller {

    @ApiOperation(value = "获得核销码基础信息", httpMethod = "POST")
    @RequestMapping("/getWriteOffCodeInfo")
    @ResponseBody
    public Object getWriteOffCodeInfo(@ApiParam(name = "orderNo", value = "订单号") @RequestParam String orderNo) {

        return Result.success("");
    }

    @ApiOperation(value = "二维码核销", httpMethod = "POST")
    @RequestMapping("/writeOffCodeExecute")
    @ResponseBody
    public Result<Object> writeOffCodeExecute(
            @ApiParam(name = "orderNo", value = "订单号") @RequestParam String orderNo,
            @ApiParam(name = "timestamp", value = "时间戳") @RequestParam String timestamp,
            @ApiParam(name = "tokenSgin", value = "token秘钥") @RequestParam String tokenSgin
    ) {

        return Result.success("");
    }
}
