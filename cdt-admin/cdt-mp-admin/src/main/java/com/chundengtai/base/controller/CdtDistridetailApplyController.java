package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.CdtDistridetailApply;
import com.chundengtai.base.service.CdtDistridetailApplyService;
import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.service.admin.ApplyCashService;
import com.chundengtai.base.transfer.BaseForm;
import com.chundengtai.base.utils.Base64;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.ShiroUtils;
import com.chundengtai.base.weixinapi.DistributionStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cdtDistridetailApply")
@Slf4j
public class CdtDistridetailApplyController {

    @Autowired
    private CdtDistridetailApplyService cdtDistridetailApplyService;

    @Autowired
    private ApplyCashService applyCashService;

    @Autowired
    private CdtDistridetailService cdtDistridetailService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
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
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtDistridetailApply> collectList = cdtDistridetailApplyService.list(conditon);
        collectList.stream().forEach(e -> {
            e.setUserName(Base64.decode(e.getUserName()));
        });
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")

    public R info(@PathVariable("id") Integer id) {
        CdtDistridetailApply model = cdtDistridetailApplyService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")

    public R save(@RequestBody CdtDistridetailApply paramModel) {
        boolean result = cdtDistridetailApplyService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    public R update(@RequestBody CdtDistridetailApply paramModel) {
        boolean result = cdtDistridetailApplyService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 批量审核
     */
    @PostMapping("/reviewModel.json")
    public R review(@RequestBody Integer[] ids) {
        List<CdtDistridetailApply> list = cdtDistridetailApplyService.listByIds(Arrays.asList(ids));
        list = list.stream().filter(e -> e.getStatus() == 403).collect(Collectors.toList());
        if (list != null) {
            list.forEach(e -> {
                String weixinOpenid = e.getWeixinOpenid();
                String realName = e.getRealName();
                double money = e.getMoney().doubleValue();
                R result = applyCashService.wechatMoneyToUser(weixinOpenid, realName, money);
                log.info("===审核===" + result);
                if (result.get("code").equals(0)) {
                    try {
                        //更新分销审核表类型
                        e.setStatus(DistributionStatus.COMPLETED_GETGOLD.getCode());
                        e.setUpdateTime(new Date());
                        e.setOperator(ShiroUtils.getUserEntity().getUsername());
                        //更新分销订单类型
                        CdtDistridetail cdtDistridetail = cdtDistridetailService.getById(e.getId());
                        cdtDistridetail.setUpdateTime(new Date());
                        cdtDistridetail.setStatus(DistributionStatus.COMPLETED_GETGOLD.getCode());
                        cdtDistridetailService.updateById(cdtDistridetail);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    e.setRemark(result.get("msg").toString());

                }
                cdtDistridetailApplyService.updateById(e);
            });
        }
        return R.ok();
    }
}
