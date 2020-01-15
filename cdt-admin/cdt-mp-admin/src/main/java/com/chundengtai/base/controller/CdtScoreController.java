package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtScore;
import com.chundengtai.base.service.CdtScoreService;
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
@RequestMapping("/cdtScore")
public class CdtScoreController {
    @Autowired
    public CdtScoreService cdtScoreService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
    public R list(@RequestBody BaseForm<CdtScore> params) {
        QueryWrapper<CdtScore> conditon = new QueryWrapper<>();
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
        if (params.getData().getScore() != null) {
            conditon.eq("score", params.getData().getScore());
        }
        //String=====
        if (params.getData().getDetail() != null) {
            conditon.eq("detail", params.getData().getDetail());
        }
        //BigDecimal=====
        if (params.getData().getMoney() != null) {
            conditon.eq("money", params.getData().getMoney());
        }
        //BigDecimal=====
        if (params.getData().getOffsetMoney() != null) {
            conditon.eq("offset_money", params.getData().getOffsetMoney());
        }
        //Boolean=====
        if (params.getData().getType() != null) {
            conditon.eq("type", params.getData().getType());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtScore> collectList = cdtScoreService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
    public R info(@PathVariable("id") Integer id) {
        CdtScore model = cdtScoreService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
    public R save(@RequestBody CdtScore paramModel) {
        boolean result = cdtScoreService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    public R update(@RequestBody CdtScore paramModel) {
        boolean result = cdtScoreService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtScoreService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}
