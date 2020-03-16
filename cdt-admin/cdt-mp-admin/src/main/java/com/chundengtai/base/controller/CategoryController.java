package com.chundengtai.base.controller;

import com.chundengtai.base.entity.CategoryEntity;
import com.chundengtai.base.service.admin.CategoryService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.TreeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("category:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<CategoryEntity> categoryList = categoryService.queryList(query);
        int total = categoryService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(categoryList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("category:info")
    public R info(@PathVariable("id") Integer id) {
        CategoryEntity category = categoryService.queryObject(id);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("category:save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("category:update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.update(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("category:delete")
    public R delete(@RequestBody Integer[] ids) {
        categoryService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<CategoryEntity> list = categoryService.queryList(params);
        //添加顶级菜单
        CategoryEntity root = new CategoryEntity();
        root.setId(0);
        root.setName("一级分类");
        root.setParentId(-1);
        root.setOpen(true);
        list.add(0, root);
        return R.ok().put("list", list);
    }

    /**
     * 查看信息(全部加载页面渲染太慢！)
     */
    @RequestMapping("/getAreaTree")
    public R getAreaTree() {
        List<CategoryEntity> list = categoryService.queryList(new HashMap<String, Object>());
        for (CategoryEntity sysRegionEntity : list) {
            sysRegionEntity.setValue(sysRegionEntity.getId() + "");
            sysRegionEntity.setLabel(sysRegionEntity.getName());
        }
        List<CategoryEntity> node = TreeUtils.factorTree(list);

        return R.ok().put("node", node);
    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping("/getCategorySelect")
    public R getCategorySelect() {
        Map<String, Object> map = new HashMap<>();

        List<CategoryEntity> list = categoryService.queryList(map);
        return R.ok().put("list", list);
    }
}
