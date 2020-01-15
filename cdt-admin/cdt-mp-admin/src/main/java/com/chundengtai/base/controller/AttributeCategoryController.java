package com.chundengtai.base.controller;

import com.chundengtai.base.entity.AttributeCategoryEntity;
import com.chundengtai.base.service.admin.AttributeCategoryService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("attributecategory")
public class AttributeCategoryController {
    @Autowired
    private AttributeCategoryService attributeCategoryService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("attributecategory:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<AttributeCategoryEntity> attributeCategoryList = attributeCategoryService.queryList(query);
        int total = attributeCategoryService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(attributeCategoryList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("attributecategory:info")
    public R info(@PathVariable("id") Integer id) {
        AttributeCategoryEntity attributeCategory = attributeCategoryService.queryObject(id);
        return R.ok().put("attributeCategory", attributeCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("attributecategory:save")
    public R save(@RequestBody AttributeCategoryEntity attributeCategory) {
        attributeCategoryService.save(attributeCategory);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("attributecategory:update")
    public R update(@RequestBody AttributeCategoryEntity attributeCategory) {
        attributeCategoryService.update(attributeCategory);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("attributecategory:delete")
    public R delete(@RequestBody Integer[] ids) {
        attributeCategoryService.deleteBatch(ids);
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {
        params.put("enabled", 1);
        List<AttributeCategoryEntity> list = attributeCategoryService.queryList(params);
        return R.ok().put("list", list);
    }
}
