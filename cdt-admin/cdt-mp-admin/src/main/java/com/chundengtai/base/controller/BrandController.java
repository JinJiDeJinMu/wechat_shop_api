package com.chundengtai.base.controller;

import com.chundengtai.base.entity.BrandEntity;
import com.chundengtai.base.entity.SysUserEntity;
import com.chundengtai.base.service.admin.BrandService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("brand")
public class BrandController extends BaseController {
    @Autowired
    private BrandService brandService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("brand:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = getqCurrentQuery(params);
        List<BrandEntity> brandList = brandService.queryList(query);
        int total = brandService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(brandList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("brand:info")
    public R info(@PathVariable("id") Integer id) {
        BrandEntity brand = brandService.queryObject(id);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("brand:save")
    public R save(@RequestBody BrandEntity brand) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        brand.setMerchantId(sysUserEntity.getMerchantId());
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("brand:update")
    public R update(@RequestBody BrandEntity brand) {
        brandService.update(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("brand:delete")
    public R delete(@RequestBody Integer[] ids) {
        brandService.deleteBatch(ids);
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        params.put("merchantId", sysUserEntity.getMerchantId());
        List<BrandEntity> list = brandService.queryList(params);
        return R.ok().put("list", list);
    }
}
