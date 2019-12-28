package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistridetailApply;
import com.chundengtai.base.service.CdtDistridetailApplyService;
import com.chundengtai.base.transfer.BaseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.service.ApplyCashService;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cdtDistridetailApply")
public class CdtDistridetailApplyController {

    @Autowired
    private CdtDistridetailApplyService cdtDistridetailApplyService;

    @Autowired
    private ApplyCashService applyCashService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
    //@RequiresPermissions("cdtdistridetailapply:list")
    public R list(@RequestBody BaseForm<CdtDistridetailApply> params) {
        QueryWrapper<CdtDistridetailApply> conditon = new QueryWrapper<>();
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
        if (params.getData().getOrderSn() != null) {
            conditon.eq("order_sn", params.getData().getOrderSn());
        }
        if (params.getData().getStatus() != null) {
            conditon.eq("status", params.getData().getStatus());
        }
        if (params.getData().getWeixinOpenid() != null) {
            conditon.eq("weixinOpenid", params.getData().getWeixinOpenid());
        }
        if (params.getData().getUserName() != null) {
            conditon.like("userName", params.getData().getUserName());
        }
        if (params.getData().getRealName() != null) {
            conditon.like("realName", params.getData().getRealName());
        }
        if (params.getData().getOperator() != null) {
            conditon.like("operator", params.getData().getOperator());
        }
        if (params.getData().getType() != null) {
            conditon.eq("type", params.getData().getType());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtDistridetailApply> collectList = cdtDistridetailApplyService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 批量审核
     */
    @PostMapping("/reviewModel.json")
    //@RequiresPermissions("cdtdistridetailapply:deleteModel")
    public R review(@RequestBody Integer[] ids) {
        List<CdtDistridetailApply> list = cdtDistridetailApplyService.listByIds(Arrays.asList(ids));
        list.parallelStream().filter(e -> e.getType() == 0).collect(Collectors.toList())
                .forEach(e -> {
                    String weixinOpenid = e.getWeixinOpenid();
                    String realName = e.getRealName();
                    double money = e.getMoney().doubleValue();
                    if (applyCashService.wechatMoneyToUser(weixinOpenid, realName, money)) {
                        e.setType(1);
                        e.setUpdateTime(new Date());
                        e.setOperator(ShiroUtils.getUserEntity().getUsername());
                        cdtDistridetailApplyService.save(e);
                    }

                });
        return R.ok();
    }
}
