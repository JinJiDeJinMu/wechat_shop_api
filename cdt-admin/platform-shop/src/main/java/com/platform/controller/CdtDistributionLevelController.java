package com.platform.controller;

import com.chundengtai.base.bean.CdtDistributionLevel;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.CdtDistributionLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Description 分销层级处理类
 * @Author hujinbiao
 * @Date 2019年12月19日 0019 上午 10:23:53
 * @Version 1.0
 **/
@RestController
@RequestMapping("/cdt")
public class CdtDistributionLevelController {

    @Autowired
    private CdtDistributionLevelService cdtDistributionLevelService;


    @GetMapping("/DistributionLevels")
    public Result queryList(Integer pageNum, Integer pageSize) {
        return Result.success(cdtDistributionLevelService.queryList(pageNum, pageSize));
    }

    @GetMapping("/DistributionLevel/{id}")
    public Result query(@PathVariable Long id) {
        return Result.success(cdtDistributionLevelService.getById(id));
    }

    @PostMapping("/DistributionLevel")
    public Result save(@RequestBody CdtDistributionLevel cdtDistributionLevel) {
        return Result.success(cdtDistributionLevelService.addDistributionLevel(cdtDistributionLevel));
    }

    @DeleteMapping("/DistributionLevel/{id}")
    public Result delete(@PathVariable("id") Long id) {
        return Result.success(cdtDistributionLevelService.removeById(id));
    }

    @PutMapping("/DistributionLevel")
    public Result update(@RequestBody CdtDistributionLevel cdtDistributionLevel) {
        return Result.success(cdtDistributionLevelService.updateDistributionLevel(cdtDistributionLevel));
    }

}
