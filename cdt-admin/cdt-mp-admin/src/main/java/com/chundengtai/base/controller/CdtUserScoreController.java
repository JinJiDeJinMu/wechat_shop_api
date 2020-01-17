package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtUserScore;
import com.chundengtai.base.service.CdtUserScoreService;
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
@RequestMapping("/cdtUserScore")
public class CdtUserScoreController {
    @Autowired
    public CdtUserScoreService cdtUserScoreService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
    public R list(@RequestBody BaseForm<CdtUserScore> params) {
        QueryWrapper<CdtUserScore> conditon = new QueryWrapper<>();
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
        if (params.getData().getScore() != null) {
            conditon.eq("score", params.getData().getScore());
        }

        if (params.getData().getToken() != null) {
            conditon.eq("token", params.getData().getToken());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtUserScore> collectList = cdtUserScoreService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
    public R info(@PathVariable("id") Integer id) {
        CdtUserScore model = cdtUserScoreService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
    public R save(@RequestBody CdtUserScore paramModel) {
        boolean result = cdtUserScoreService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    public R update(@RequestBody CdtUserScore paramModel) {
        boolean result = cdtUserScoreService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtUserScoreService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}
