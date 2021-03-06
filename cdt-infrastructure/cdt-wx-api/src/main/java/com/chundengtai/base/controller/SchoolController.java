package com.chundengtai.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.bean.School;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 学校信息
 * @Author hujinbiao
 * @Date 2019年12月24日 0024 下午 07:42:15
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/school")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    /**
     * 查询所有学校信息
     *
     * @return
     */
    @GetMapping("/list")
    @IgnoreAuth
    public Result queryList() {
        return Result.success(schoolService.list());
    }

    /**
     * 批量添加学校信息
     *
     * @param schoolLists
     * @return
     */
    @GetMapping("/batch")
    @IgnoreAuth
    public Result saveBatch(@RequestParam String schoolLists) {
        List<School> list1 = JSONObject.parseArray(schoolLists, School.class);

        List<School> schoolList = schoolService.list();
        schoolList.stream().forEach(e -> {
            if (list1.contains(e)) {
                list1.remove(e);
            }
        });
        schoolService.saveBatch(list1);
        return Result.success();
    }

}
