package com.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.constance.ShopShow;
import com.platform.entity.CdtMerchantEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.CdtMerchantService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商家Controller
 *
 * @author lipengjun
 * @date 2019-11-15 17:08:05
 */
@Controller
@RequestMapping("cdtmerchant")
public class CdtMerchantController extends BaseController {
    @Autowired
    private CdtMerchantService cdtMerchantService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cdtmerchant:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = getqCurrentQuery(params);
        List<CdtMerchantEntity> cdtMerchantList = cdtMerchantService.queryList(query);
        int total = cdtMerchantService.queryTotal(query);
        System.out.println(cdtMerchantList);
        cdtMerchantList = cdtMerchantList.stream().map(s ->
                {
                    s.setKey(String.valueOf(s.getId()));
                    return s;
                }
        ).collect(Collectors.toList());
        PageUtils pageUtil = new PageUtils(cdtMerchantList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("cdtmerchant:info")
    @ResponseBody
    public R info(@PathVariable("id") Long id) {
        CdtMerchantEntity cdtMerchant = cdtMerchantService.queryObject(id);

        return R.ok().put("cdtMerchant", cdtMerchant);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ResponseBody
    public R save(@RequestBody CdtMerchantEntity cdtMerchant) {
        cdtMerchantService.save(cdtMerchant);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cdtmerchant:update")
    @ResponseBody
    public R update(@RequestBody CdtMerchantEntity cdtMerchant) {
        cdtMerchantService.update(cdtMerchant);
        System.out.println(cdtMerchant);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cdtmerchant:delete")
    @ResponseBody
    public R delete(@RequestBody Long[] ids) {
        cdtMerchantService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    @Ignore
    public R queryAll(@RequestParam Map<String, Object> params) {

        SysUserEntity sysUserEntity= ShiroUtils.getUserEntity();
        if (sysUserEntity.getMerchantId() == null) {
            return R.ok().put("list", null);
        }
        if (sysUserEntity.getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            params.put("merchant_id", sysUserEntity.getMerchantId());
        }
        List<CdtMerchantEntity> list = cdtMerchantService.queryList(params);

        return R.ok().put("list", list);
    }


    @RequestMapping("/open/{id}/{status}")
    @RequiresPermissions("cdtmerchant:open")
    @ResponseBody
    public R open(@PathVariable("id") Long id,@PathVariable("status") Integer status){

        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {

            return R.error("不是超级管理员，没有权限！");
        }

        CdtMerchantEntity cdtMerchantEntity = cdtMerchantService.queryObject(id);
        if(null == cdtMerchantEntity){

            return R.error("商户不存在！");
        }

        cdtMerchantEntity.setCashStatus(status);
        cdtMerchantService.update(cdtMerchantEntity);
        return R.ok();
    }

    public static void main(String[] args) {
        CdtMerchantEntity cdt = new CdtMerchantEntity();
        cdt.setShopName("ssas");
        cdt.setShopType(2);
        cdt.setCity("cdcd");
        System.out.println(JSONObject.toJSON(cdt));
    }
}
