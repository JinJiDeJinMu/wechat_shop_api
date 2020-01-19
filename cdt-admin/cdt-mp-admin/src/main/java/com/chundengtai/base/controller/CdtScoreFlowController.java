package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtScoreFlow;
import com.chundengtai.base.service.CdtScoreFlowService;
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
@RequestMapping("/cdtScoreFlow")
public class CdtScoreFlowController {
    @Autowired
    public CdtScoreFlowService cdtScoreFlowService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
    public R list(@RequestBody BaseForm<CdtScoreFlow> params) {
        QueryWrapper<CdtScoreFlow> conditon = new QueryWrapper<>();
        if (!StringUtils.isEmpty(params.getSortField()) && !StringUtils.isEmpty(params.getOrder())) {
            if (params.getOrder().equalsIgnoreCase("asc")) {
                conditon.orderByAsc(params.getSortField());
            } else {
                conditon.orderByDesc(params.getSortField());
            }
        }
        //Long=====
        if (params.getData().getId() != null) {
            conditon.eq("id", params.getData().getId());
        }
        //Integer=====
        if (params.getData().getUserId() != null) {
            conditon.eq("user_id", params.getData().getUserId());
        }
        //String=====
        if (params.getData().getFlowSn() != null) {
            conditon.eq("flow_sn", params.getData().getFlowSn());
        }
        //Integer=====
        //Integer=====
        if (params.getData().getScore() != null) {
            conditon.eq("score_sum", params.getData().getScore());
        }
        //BigDecimal=====
        if (params.getData().getMoney() != null) {
            conditon.eq("money", params.getData().getMoney());
        }

        //Integer=====
        if (params.getData().getPayStatus() != null) {
            conditon.eq("pay_status", params.getData().getPayStatus());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtScoreFlow> collectList = cdtScoreFlowService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
    public R info(@PathVariable("id") Integer id) {
        CdtScoreFlow model = cdtScoreFlowService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
    public R save(@RequestBody CdtScoreFlow paramModel) {
        boolean result = cdtScoreFlowService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    public R update(@RequestBody CdtScoreFlow paramModel) {
        boolean result = cdtScoreFlowService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtScoreFlowService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}
