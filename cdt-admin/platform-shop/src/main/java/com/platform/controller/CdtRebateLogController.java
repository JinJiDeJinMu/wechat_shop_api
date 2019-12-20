package com.platform.controller;

import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.CdtRebateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 分销记录表处理类
 * @Author hujinbiao
 * @Date 2019年12月19日 0019 下午 05:34:57
 * @Version 1.0
 **/
@RestController
@RequestMapping("/cdt")
public class CdtRebateLogController {

    @Autowired
    private CdtRebateLogService cdtRebateLogService;

    @GetMapping("/RebateLogs")
    public Result queryList(Integer pageNum, Integer pageSize) {
        return Result.success(cdtRebateLogService.queryList(pageNum, pageSize));
    }

    @GetMapping("/RebateLog")
    public Result query(@PathVariable("id") Integer id) {
        return Result.success(cdtRebateLogService.getById(id));
    }
}
