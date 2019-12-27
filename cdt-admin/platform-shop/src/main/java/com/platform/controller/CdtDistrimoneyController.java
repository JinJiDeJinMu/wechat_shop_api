package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistrimoney;
import com.chundengtai.base.jwt.JavaWebToken;
import com.chundengtai.base.service.CdtDistrimoneyService;
import com.chundengtai.base.transfer.BaseForm;
import com.chundengtai.base.utils.BeanJwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/cdtDistrimoney")
@Slf4j
public class CdtDistrimoneyController {
    @Autowired
    public CdtDistrimoneyService cdtDistrimoneyService;


    /**
     * 列表
     */
    @PostMapping("/list.json")
//@RequiresPermissions("cdtdistrimoney:list")
    public R list(@RequestBody BaseForm<CdtDistrimoney> params) {
        QueryWrapper<CdtDistrimoney> conditon = new QueryWrapper<>();
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
        if (params.getData().getFirstPercent() != null) {
            conditon.eq("first_percent", params.getData().getFirstPercent());
        }
        if (params.getData().getSecondPercent() != null) {
            conditon.eq("second_percent", params.getData().getSecondPercent());
        }
        if (params.getData().getFirstPartner() != null) {
            conditon.eq("first_partner", params.getData().getFirstPartner());
        }
        if (params.getData().getFirstPersonCondition() != null) {
            conditon.eq("first_person_condition", params.getData().getFirstPersonCondition());
        }
        if (params.getData().getSecondPartner() != null) {
            conditon.eq("second_partner", params.getData().getSecondPartner());
        }
        if (params.getData().getSecondPersonCondition() != null) {
            conditon.eq("second_person_condition", params.getData().getSecondPersonCondition());
        }
        if (params.getData().getThirdPartner() != null) {
            conditon.eq("third_partner", params.getData().getThirdPartner());
        }
        if (params.getData().getThirdTotalMoney() != null) {
            conditon.eq("third_total_money", params.getData().getThirdTotalMoney());
        }
        if (params.getData().getStatus() != null) {
            conditon.eq("status", params.getData().getStatus());
        }
        if (params.getData().getCreatedBy() != null) {
            conditon.eq("created_by", params.getData().getCreatedBy());
        }
        if (params.getData().getUpdatedBy() != null) {
            conditon.eq("updated_by", params.getData().getUpdatedBy());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtDistrimoney> collectList = cdtDistrimoneyService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
//@RequiresPermissions("cdtdistrimoney:getModel")
    public R info(@PathVariable("id") Integer id) {
        CdtDistrimoney model = cdtDistrimoneyService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
//@RequiresPermissions("cdtdistrimoney:saveModel")
    public R save(@RequestBody CdtDistrimoney paramModel) {
        paramModel.setCreatedTime(new Date());
        paramModel.setCreatedBy(ShiroUtils.getUserEntity().getUsername());
        paramModel.setToken(null);
        paramModel.setToken(encryt(paramModel));
        boolean result = cdtDistrimoneyService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
//@RequiresPermissions("cdtdistrimoney:updateModel")
    public R update(@RequestBody CdtDistrimoney paramModel) {
        Integer id = paramModel.getId();
        paramModel.setId(null);
        paramModel.setToken(null);
        paramModel.setUpdatedTime(new Date());
        paramModel.setUpdatedBy(ShiroUtils.getUserEntity().getUsername());
        String token = encryt(paramModel);
        paramModel.setToken(token);
        paramModel.setId(id);
        boolean result = cdtDistrimoneyService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
//@RequiresPermissions("cdtdistrimoney:deleteModel")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtDistrimoneyService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }

    private String encryt(Object logModel) {
        try {
            Map chain = BeanJwtUtil.javabean2map(logModel);
            return JavaWebToken.createJavaWebToken(chain);
        } catch (Exception e) {
            log.error("jwt加密异常========");
            e.printStackTrace();
        }
        return "";
    }
}
