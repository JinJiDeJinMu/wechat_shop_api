package com.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.chundengtai.base.bean.School;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 学校信息
 * @Author hujinbiao
 * @Date 2019年12月24日 0024 下午 07:42:15
 * @Version 1.0
 **/
@RestController("/school")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    /**
     * 查询所有学校信息
     *
     * @return
     */
    @GetMapping("/list")
    public Result queryList() {
        return Result.success(schoolService.list());
    }

    /**
     * 批量添加学校信息
     *
     * @param schoolLists
     * @return
     */
    @PostMapping("/batch")
    public Result saveBatch(String schoolLists) {
        schoolService.saveBatch(JSONObject.parseArray(schoolLists, School.class));
        return Result.success();
    }
}
