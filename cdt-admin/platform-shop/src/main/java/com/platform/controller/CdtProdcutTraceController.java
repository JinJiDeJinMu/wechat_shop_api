package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtProdcutTrace;
import com.chundengtai.base.service.CdtProdcutTraceService;
import com.chundengtai.base.transfer.BaseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    @RequiresPermissions("cdtprodcuttrace:list")
    public R list(@RequestBody BaseForm<CdtProdcutTrace> params) {
        QueryWrapper<CdtProdcutTrace> conditon = new QueryWrapper<>();
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
        //String=====
        if (params.getData().getName() != null) {
            conditon.eq("name", params.getData().getName());
        }
        //String=====
        if (params.getData().getMainImage() != null) {
            conditon.eq("main_image", params.getData().getMainImage());
        }
        //String=====
        if (params.getData().getFigureImage() != null) {
            conditon.eq("figure_image", params.getData().getFigureImage());
        }
        //String=====
        if (params.getData().getQrcode() != null) {
            conditon.eq("qrcode", params.getData().getQrcode());
        }
        //String=====
        if (params.getData().getMpQrcode() != null) {
            conditon.eq("mp_qrcode", params.getData().getMpQrcode());
        }
        //String=====
        if (params.getData().getOriginGoods() != null) {
            conditon.eq("origin_goods", params.getData().getOriginGoods());
        }
        //String=====
        if (params.getData().getHeight() != null) {
            conditon.eq("height", params.getData().getHeight());
        }
        //String=====
        if (params.getData().getProduceDateCode() != null) {
            conditon.eq("produce_date_code", params.getData().getProduceDateCode());
        }
        //String=====
        if (params.getData().getExpirationDate() != null) {
            conditon.eq("expiration_date", params.getData().getExpirationDate());
        }
        //String=====
        if (params.getData().getStorageCondition() != null) {
            conditon.eq("storage_condition", params.getData().getStorageCondition());
        }
        //String=====
        if (params.getData().getPlatformBatchNumber() != null) {
            conditon.eq("platform_batch_number", params.getData().getPlatformBatchNumber());
        }
        //String=====
        if (params.getData().getProductBrief() != null) {
            conditon.eq("product_brief", params.getData().getProductBrief());
        }
        //Integer=====
        if (params.getData().getAccessCount() != null) {
            conditon.eq("access_count", params.getData().getAccessCount());
        }
        //Date=====
        //String=====
        if (params.getData().getProductionEnterpriseNo() != null) {
            conditon.eq("production_enterprise_no", params.getData().getProductionEnterpriseNo());
        }
        //String=====
        if (params.getData().getResponsibilityName() != null) {
            conditon.eq("responsibility_name", params.getData().getResponsibilityName());
        }
        //String=====
        if (params.getData().getResponsibilityAddress() != null) {
            conditon.eq("responsibility_address", params.getData().getResponsibilityAddress());
        }
        //String=====
        if (params.getData().getManufacturerName() != null) {
            conditon.eq("manufacturer_name", params.getData().getManufacturerName());
        }
        //String=====
        if (params.getData().getManufacturerAddress() != null) {
            conditon.eq("manufacturer_address", params.getData().getManufacturerAddress());
        }
        //String=====
        if (params.getData().getDistributorName() != null) {
            conditon.eq("distributor_name", params.getData().getDistributorName());
        }
        //String=====
        if (params.getData().getDistributorAddress() != null) {
            conditon.eq("distributor_address", params.getData().getDistributorAddress());
        }
        //String=====
        if (params.getData().getDistributorContact() != null) {
            conditon.eq("distributor_contact", params.getData().getDistributorContact());
        }
        //String=====
        if (params.getData().getMaterialInfo() != null) {
            conditon.eq("material_info", params.getData().getMaterialInfo());
        }
        //String=====
        if (params.getData().getMaterialCountryOrigin() != null) {
            conditon.eq("material_country_origin", params.getData().getMaterialCountryOrigin());
        }
        //Integer=====
        if (params.getData().getRevision() != null) {
            conditon.eq("revision", params.getData().getRevision());
        }
        //String=====
        if (params.getData().getCreatedBy() != null) {
            conditon.eq("created_by", params.getData().getCreatedBy());
        }
        //Date=====
        //String=====
        if (params.getData().getUpdatedBy() != null) {
            conditon.eq("updated_by", params.getData().getUpdatedBy());
        }
        //Date=====
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<CdtProdcutTrace> collectList = cdtProdcutTraceService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
    @RequiresPermissions("cdtprodcuttrace:getModel")
    public R info(@PathVariable("id") Integer id) {
        CdtProdcutTrace model = cdtProdcutTraceService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @PostMapping("/saveModel.json")
    @RequiresPermissions("cdtprodcuttrace:saveModel")
    public R save(@RequestBody CdtProdcutTrace paramModel) {
        boolean result = cdtProdcutTraceService.save(paramModel);
        return R.ok(result);
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    @RequiresPermissions("cdtprodcuttrace:updateModel")
    public R update(@RequestBody CdtProdcutTrace paramModel) {
        boolean result = cdtProdcutTraceService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
    @RequiresPermissions("cdtprodcuttrace:deleteModel")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = cdtProdcutTraceService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }
}
