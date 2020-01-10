package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.transfer.BaseForm;
import com.chundengtai.base.utils.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cdtDistridetail")
public class CdtDistridetailController {
    @Autowired
    public CdtDistridetailService cdtDistridetailService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
    //@RequiresPermissions("CdtDistridetail:list")
    public R list(@RequestBody BaseForm<CdtDistridetail> params) {
        QueryWrapper<CdtDistridetail> conditon = new QueryWrapper<>();
        if (!StringUtils.isEmpty(params.getSortField()) && !StringUtils.isEmpty(params.getOrder())) {
            if (params.getOrder().equalsIgnoreCase("asc")) {
                conditon.orderByAsc(params.getSortField());
            } else {
                conditon.orderByDesc(params.getSortField());
            }
        }
        if (params.getData().getUserId() != null) {
            conditon.eq("user_id", params.getData().getUserId());
        }
        if (params.getData().getGoldUserId() != null) {
            conditon.eq("gold_user_id", params.getData().getGoldUserId());
        }
        if (params.getData().getOrderSn() != null) {
            conditon.eq("order_sn", params.getData().getOrderSn());
        }
        if (params.getData().getStatus() != null) {
            conditon.eq("status", params.getData().getStatus());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtDistridetail> collectList = cdtDistridetailService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
    //@RequiresPermissions("CdtDistridetail:getModel")
    public R info(@PathVariable("id") Integer id) {
        CdtDistridetail model = cdtDistridetailService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
    //@RequiresPermissions("CdtDistridetail:saveModel")
    public R save(@RequestBody CdtDistridetail paramModel) {
        boolean result = cdtDistridetailService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    //@RequiresPermissions("CdtDistridetail:updateModel")
    public R update(@RequestBody CdtDistridetail paramModel) {
        boolean result = cdtDistridetailService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
    //@RequiresPermissions("CdtDistridetail:deleteModel")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtDistridetailService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}

