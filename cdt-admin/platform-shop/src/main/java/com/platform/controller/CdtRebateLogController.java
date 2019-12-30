package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtRebateLog;
import com.chundengtai.base.service.CdtRebateLogService;
import com.chundengtai.base.transfer.BaseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.utils.Base64;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/cdtRebateLog")
public class CdtRebateLogController {
    @Autowired
    public CdtRebateLogService cdtRebateLogService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
//@RequiresPermissions("cdtrebatelog:list")
    public R list(@RequestBody BaseForm<CdtRebateLog> params) {
        QueryWrapper<CdtRebateLog> conditon = new QueryWrapper<>();
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
        if (params.getData().getGoldUserId() != null) {
            conditon.eq("gold_user_id", params.getData().getGoldUserId());
        }
        //Integer=====
        if (params.getData().getBuyUserId() != null) {
            conditon.eq("buy_user_id", params.getData().getBuyUserId());
        }
        //String=====
        if (params.getData().getNickname() != null) {
            conditon.eq("nickname", params.getData().getNickname());
        }
        //Integer=====
        if (params.getData().getOrderId() != null) {
            conditon.eq("order_id", params.getData().getOrderId());
        }
        //String=====
        if (params.getData().getOrderSn() != null) {
            conditon.eq("order_sn", params.getData().getOrderSn());
        }
        //BigDecimal=====
        if (params.getData().getGoodsPrice() != null) {
            conditon.eq("goods_price", params.getData().getGoodsPrice());
        }
        //BigDecimal=====
        if (params.getData().getMoney() != null) {
            conditon.eq("money", params.getData().getMoney());
        }
        //Integer=====
        if (params.getData().getLevel() != null) {
            conditon.eq("level", params.getData().getLevel());
        }
        //Date=====
        //Integer=====
        if (params.getData().getStatus() != null) {
            conditon.eq("status", params.getData().getStatus());
        }
        //Date=====
        //String=====
        if (params.getData().getRemark() != null) {
            conditon.eq("remark", params.getData().getRemark());
        }
        //Long=====
        if (params.getData().getMechantId() != null) {
            conditon.eq("mechant_id", params.getData().getMechantId());
        }
        //String=====
        if (params.getData().getToken() != null) {
            conditon.eq("token", params.getData().getToken());
        }
        //Date=====
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtRebateLog> collectList = cdtRebateLogService.list(conditon);
        collectList = collectList.stream().map(e -> {
            e.setNickname(Base64.decode(e.getNickname()));
            return e;
        }).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
//@RequiresPermissions("cdtrebatelog:getModel")
    public R info(@PathVariable("id") Integer id) {
        CdtRebateLog model = cdtRebateLogService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
//@RequiresPermissions("cdtrebatelog:saveModel")
    public R save(@RequestBody CdtRebateLog paramModel) {
        boolean result = cdtRebateLogService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
//@RequiresPermissions("cdtrebatelog:updateModel")
    public R update(@RequestBody CdtRebateLog paramModel) {
        boolean result = cdtRebateLogService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
    @RequiresPermissions("cdtrebatelog:deleteModel")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtRebateLogService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}
