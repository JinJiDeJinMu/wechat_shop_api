//package com.chundengtai.base.server.controller;
//
//import io.swagger.annotations.*;
//import org.chenguoyu.learn.springboot.mybatis.User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import springfox.documentation.annotations.ApiIgnore;
//
//import java.time.LocalDate;
//
//@RestController
//@RequestMapping("/swagger")
//@Api(value = "swaggerDemo1", tags = "swagger注解实例名称", description = "swagger注解示例描述")
//public class SwaggerDemo {
//
//    @ApiOperation(value = "方法的用途和作用", notes = "方法的注意事项和备注", tags = "标签",response = User.class)
//    @GetMapping("/apiOperation")
//    public User apiOperation() {
//        User user = new User(1,"张三",1,18, LocalDate.of(1996,1,9));
//        return user;
//    }
//
//    @ApiOperation(value = "apiImplicitParam示例", response = User.class)
//    @ApiImplicitParam(name = "user", value = "用户类-参数说明", dataType = "org.chenguoyu.learn.springboot.mybatis.User")
//    @GetMapping("/apiImplicitParam")
//    public User apiImplicitParam(@RequestBody User user) {
//        return user;
//    }
//
//    @ApiOperation(value = "apiImplicitParams示例")
//    @GetMapping("/apiParam")
//    @ApiResponses(
//            @ApiResponse(code = 200,message = "成功")
//    )
//    public User apiParam(@RequestBody @ApiParam(name = "user", value = "用户类-参数说明") User user) {
//        return user;
//    }
//
//    @ApiIgnore
//    @GetMapping("/apiIgnore")
//    public User apiIgnore(@RequestBody User user) {
//        return user;
//    }
//}