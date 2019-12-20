package com.platform.controller;


import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.CdtDistridetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 用户购买分销得钱处理类
 * @Author hujinbiao
 * @Date 2019年12月19日 0019 下午 03:13:18
 * @Version 1.0
 **/
@RestController
@RequestMapping("/cdt")
public class CdtDistridetailController {

    @Autowired
    private CdtDistridetailService cdtDistridetailService;

    @GetMapping("/Distridetails")
    public Result queryList(Integer pageNum, Integer pageSize) {
        return Result.success(cdtDistridetailService.queryList(pageNum, pageSize));
    }

    @GetMapping("/Distridetail/{id}")
    public Result query(@PathVariable("id") Long id) {
        return Result.success(cdtDistridetailService.getById(id));
    }

    @PostMapping("/Distridetail")
    public Result saveCdtDistridetail(@RequestBody CdtDistridetail cdtDistridetail) {
        cdtDistridetailService.saveCdtDistridetail(cdtDistridetail);
        return Result.success();
    }

}
