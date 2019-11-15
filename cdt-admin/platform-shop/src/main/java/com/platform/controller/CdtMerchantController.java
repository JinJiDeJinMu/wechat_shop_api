package com.platform.controller;

import com.platform.entity.CdtMerchantEntity;
import com.platform.service.CdtMerchantService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商家Controller
 *
 * @author lipengjun
 * @date 2019-11-15 17:08:05
 */
@Controller
@RequestMapping("cdtmerchant")
public class CdtMerchantController {
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
        Query query = new Query(params);

        List<CdtMerchantEntity> cdtMerchantList = cdtMerchantService.queryList(query);
        int total = cdtMerchantService.queryTotal(query);

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
    @RequiresPermissions("cdtmerchant:save")
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
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<CdtMerchantEntity> list = cdtMerchantService.queryList(params);

        return R.ok().put("list", list);
    }
}
