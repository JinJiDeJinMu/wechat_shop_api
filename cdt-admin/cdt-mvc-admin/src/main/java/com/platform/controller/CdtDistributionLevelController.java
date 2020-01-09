package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistributionLevel;
import com.chundengtai.base.service.CdtDistributionLevelService;
import com.chundengtai.base.transfer.BaseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cdtDistributionLevel")
public class CdtDistributionLevelController {
    @Autowired
    public CdtDistributionLevelService cdtDistributionLevelService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
//@RequiresPermissions("cdtdistributionlevel:list")
    public R list(@RequestBody BaseForm<CdtDistributionLevel> params) {
        QueryWrapper<CdtDistributionLevel> conditon = new QueryWrapper<>();
        if (!StringUtils.isEmpty(params.getSortField()) && !StringUtils.isEmpty(params.getOrder())) {
            if (params.getOrder().equalsIgnoreCase("asc")) {
                conditon.orderByAsc(params.getSortField());
            } else {
                conditon.orderByDesc(params.getSortField());
            }
        }
        if (params.getData().getId() != null) {
            conditon.eq("id", params.getData().getId());
        }
        if (params.getData().getUserId() != null) {
            conditon.eq("user_id", params.getData().getUserId());
        }
        if (params.getData().getParentId() != null) {
            conditon.eq("parent_id", params.getData().getParentId());
        }
        if (params.getData().getFxLevel() != null) {
            conditon.eq("fx_level", params.getData().getFxLevel());
        }
        if (params.getData().getSponsorId() != null) {
            conditon.eq("sponsor_id", params.getData().getSponsorId());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtDistributionLevel> collectList = cdtDistributionLevelService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
//@RequiresPermissions("cdtdistributionlevel:getModel")
    public R info(@PathVariable("id") Integer id) {
        CdtDistributionLevel model = cdtDistributionLevelService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
//@RequiresPermissions("cdtdistributionlevel:saveModel")
    public R save(@RequestBody CdtDistributionLevel paramModel) {
        boolean result = cdtDistributionLevelService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    @RequiresPermissions("cdtdistributionlevel:updateModel")
    public R update(@RequestBody CdtDistributionLevel paramModel) {
        boolean result = cdtDistributionLevelService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
//@RequiresPermissions("cdtdistributionlevel:deleteModel")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtDistributionLevelService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}
