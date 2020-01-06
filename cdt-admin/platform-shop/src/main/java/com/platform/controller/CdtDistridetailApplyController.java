package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.CdtDistridetailApply;
import com.chundengtai.base.service.CdtDistridetailApplyService;
import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.transfer.BaseForm;
import com.chundengtai.base.weixinapi.DistributionStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.service.ApplyCashService;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
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
        list = list.stream().filter(e -> e.getStatus() == 405).collect(Collectors.toList());
        log.info("======" + list);
        list.forEach(e -> {
                    String weixinOpenid = e.getWeixinOpenid();
                    String realName = e.getRealName();
                    double money = e.getMoney().doubleValue();
            log.info("===开始审核===" + e);
                    if (applyCashService.wechatMoneyToUser(weixinOpenid, realName, money)) {
                        try {
                            //更新分销审核表类型
                            e.setStatus(DistributionStatus.COMPLETED_GETGOLD.getCode());
                            e.setUpdateTime(new Date());
                            e.setOperator(ShiroUtils.getUserEntity().getUsername());
                            cdtDistridetailApplyService.updateById(e);
                            //更新分销订单类型
                            CdtDistridetail cdtDistridetail = cdtDistridetailService.getById(e.getId());
                            cdtDistridetail.setUpdateTime(new Date());
                            cdtDistridetail.setStatus(DistributionStatus.COMPLETED_GETGOLD.getCode());
                            /*cdtDistridetail.setId(null);
                            cdtDistridetail.setToken(null);
                            String token = JavaWebToken.createJavaWebToken(BeanJwtUtil.javabean2map(cdtDistridetail));
                            cdtDistridetail.setToken(token);
                            cdtDistridetail.setId(e.getId());*/
                            cdtDistridetailService.updateById(cdtDistridetail);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                });
        return R.ok();
    }
}
