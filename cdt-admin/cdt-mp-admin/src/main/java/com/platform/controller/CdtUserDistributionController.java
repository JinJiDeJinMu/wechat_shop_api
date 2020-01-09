package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtUserDistribution;
import com.chundengtai.base.service.CdtUserDistributionService;
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
@RequestMapping("/cdtUserDistribution")
public class CdtUserDistributionController {
    @Autowired
    public CdtUserDistributionService cdtUserDistributionService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
//@RequiresPermissions("cdtuserdistribution:list")
    public R list(@RequestBody BaseForm<CdtUserDistribution> params) {
        QueryWrapper<CdtUserDistribution> conditon = new QueryWrapper<>();
        if (!StringUtils.isEmpty(params.getSortField()) && !StringUtils.isEmpty(params.getOrder())) {
            if (params.getOrder().equalsIgnoreCase("asc")) {
                conditon.orderByAsc(params.getSortField());
            } else {
                conditon.orderByDesc(params.getSortField());
            }
        }
        //Integer=====
        if (params.getData().getId() != null) {
            conditon.eq("id", params.getData().getId());
        }
        //Integer=====
        if (params.getData().getUserId() != null) {
            conditon.eq("user_id", params.getData().getUserId());
        }
        //String=====
        if (params.getData().getUserName() != null) {
            conditon.like("user_name", params.getData().getUserName());
        }
        //Integer=====
        if (params.getData().getGoodsId() != null) {
            conditon.eq("goods_id", params.getData().getGoodsId());
        }
        //String=====
        if (params.getData().getGoodsName() != null) {
            conditon.eq("goods_name", params.getData().getGoodsName());
        }
        //Integer=====
        if (params.getData().getSpecId() != null) {
            conditon.eq("spec_id", params.getData().getSpecId());
        }
        //Integer=====
        if (params.getData().getShareNum() != null) {
            conditon.eq("share_num", params.getData().getShareNum());
        }
        //Integer=====
        if (params.getData().getSalesNum() != null) {
            conditon.eq("sales_num", params.getData().getSalesNum());
        }
        //Long=====
        if (params.getData().getMechantId() != null) {
            conditon.eq("mechant_id", params.getData().getMechantId());
        }
        //String=====
        if (params.getData().getCreatedBy() != null) {
            conditon.eq("created_by", params.getData().getCreatedBy());
        }
        //Date=====
        //String=====
        if (params.getData().getToken() != null) {
            conditon.eq("token", params.getData().getToken());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtUserDistribution> collectList = cdtUserDistributionService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
//@RequiresPermissions("cdtuserdistribution:getModel")
    public R info(@PathVariable("id") Integer id) {
        CdtUserDistribution model = cdtUserDistributionService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
    @RequiresPermissions("cdtuserdistribution:saveModel")
    public R save(@RequestBody CdtUserDistribution paramModel) {
        boolean result = cdtUserDistributionService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
//@RequiresPermissions("cdtuserdistribution:updateModel")
    public R update(@RequestBody CdtUserDistribution paramModel) {
        boolean result = cdtUserDistributionService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
//@RequiresPermissions("cdtuserdistribution:deleteModel")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtUserDistributionService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}
