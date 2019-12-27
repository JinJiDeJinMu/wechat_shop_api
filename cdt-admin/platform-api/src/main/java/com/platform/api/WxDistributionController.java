package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(tags = "v2分销中心管理")
@RestController
@RequestMapping("/apis/v2/distribution")
public class WxDistributionController {

    @ApiOperation(value = "分销中心个人发展信息汇总", httpMethod = "GET")
    @GetMapping("/getUserDistributionInfo.json")
    @ResponseBody
    @IgnoreAuth
    public R getUserDistributionInfo(@RequestParam Integer userId) {

        return R.ok();
    }

    @ApiOperation(value = "分销中心个人下线人数列表", httpMethod = "GET")
    @GetMapping("/getUserGroupList.json")
    @ResponseBody
    @IgnoreAuth
    public R getUserGroupList(Integer pageIndex, Integer pageSize) {
        return R.ok();
    }

    @ApiOperation(value = "分销中心个人收益列表", httpMethod = "GET")
    @GetMapping("/getUserEarnings.json")
    @ResponseBody
    @IgnoreAuth
    public R getUserInfoarnings(Integer pageIndex, Integer pageSize) {
        return R.ok();
    }

    @ApiOperation(value = "分销中心个人分享的订单列表", httpMethod = "GET")
    @GetMapping("/getUserShareOrder.json")
    @ResponseBody
    @IgnoreAuth
    public R getUserShareOrder(Integer pageIndex, Integer pageSize) {
        return R.ok();
    }
}
