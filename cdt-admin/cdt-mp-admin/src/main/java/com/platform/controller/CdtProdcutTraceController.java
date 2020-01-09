package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtProdcutTrace;
import com.chundengtai.base.service.CdtProdcutTraceService;
import com.chundengtai.base.transfer.BaseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cdtProdcutTrace")
public class CdtProdcutTraceController {
    @Autowired
    public CdtProdcutTraceService cdtProdcutTraceService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
    //@RequiresPermissions("cdtprodcuttrace:list")
    public R list(@RequestBody BaseForm<CdtProdcutTrace> params) {
        QueryWrapper<CdtProdcutTrace> conditon = new QueryWrapper<>();
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
        if (params.getData().getName() != null) {
            conditon.like("name", params.getData().getName());
        }
        if (params.getData().getMpQrcode() != null) {
            conditon.eq("mp_qrcode", params.getData().getMpQrcode());
        }
        if (params.getData().getOriginGoods() != null) {
            conditon.like("origin_goods", params.getData().getOriginGoods());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtProdcutTrace> collectList = cdtProdcutTraceService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
    //@RequiresPermissions("cdtprodcuttrace:getModel")
    public R info(@PathVariable("id") Integer id) {
        CdtProdcutTrace model = cdtProdcutTraceService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
    // @RequiresPermissions("cdtprodcuttrace:saveModel")
    public R save(@RequestBody CdtProdcutTrace paramModel) {
        boolean result = cdtProdcutTraceService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    //@RequiresPermissions("cdtprodcuttrace:updateModel")
    public R update(@RequestBody CdtProdcutTrace paramModel) {
        boolean result = cdtProdcutTraceService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
    //@RequiresPermissions("cdtprodcuttrace:deleteModel")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtProdcutTraceService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}