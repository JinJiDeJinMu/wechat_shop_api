package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.service.ApiUserService;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册
 */
@Api(tags = "注册")
@RestController
@RequestMapping("/api/register")
public class ApiRegisterController {
    @Autowired
    private ApiUserService userService;

    /**
     * 注册
     */
    @ApiOperation(value = "注册")
    @IgnoreAuth
    @PostMapping("register")
    public R register(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        userService.save(mobile, password);

        return R.ok();
    }
}
