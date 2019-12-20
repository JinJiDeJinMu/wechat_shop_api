package com.platform.controller;

import com.chundengtai.base.bean.CdtDistrimoney;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.CdtDistrimoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 分销比例处理类
 * @Author hujinbiao
 * @Date 2019年12月19日 0019 下午 04:59:37
 * @Version 1.0
 **/
@RestController
@RequestMapping("/cdt")
public class CdtDistrimoneyController {

    @Autowired
    private CdtDistrimoneyService cdtDistrimoneyService;

    @GetMapping("/Distrimoneys")
    public Result queryList(Integer pageNum, Integer pageSize) {
        return Result.success(cdtDistrimoneyService.queryList(pageNum, pageSize));
    }

    @GetMapping("/Distrimoney/{id}")
    public Result query(@PathVariable("id") Integer id) {
        return Result.success(cdtDistrimoneyService.getById(id));
    }

    @PostMapping("/Distrimoney")
    public Result save(@RequestBody CdtDistrimoney cdtDistrimoney) {
        return Result.success(cdtDistrimoneyService.addCdtDistrimoney(cdtDistrimoney));
    }

    @PutMapping("/Distrimoney")
    public Result update(@RequestBody CdtDistrimoney cdtDistrimoney) {
        return Result.success(cdtDistrimoneyService.updateDistrimoney(cdtDistrimoney));
    }

    @DeleteMapping("/Distrimoney/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(cdtDistrimoneyService.removeById(id));
    }
}
