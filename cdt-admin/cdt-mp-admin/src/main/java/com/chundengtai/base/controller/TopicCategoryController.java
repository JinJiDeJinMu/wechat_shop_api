package com.chundengtai.base.controller;

import com.chundengtai.base.entity.TopicCategoryEntity;
import com.chundengtai.base.service.admin.TopicCategoryService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("topiccategory")
public class TopicCategoryController {
    @Autowired
    private TopicCategoryService topicCategoryService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("topiccategory:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<TopicCategoryEntity> topicCategoryList = topicCategoryService.queryList(query);
        int total = topicCategoryService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(topicCategoryList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("topiccategory:info")
    public R info(@PathVariable("id") Integer id) {
        TopicCategoryEntity topicCategory = topicCategoryService.queryObject(id);

        return R.ok().put("topicCategory", topicCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("topiccategory:save")
    public R save(@RequestBody TopicCategoryEntity topicCategory) {
        topicCategoryService.save(topicCategory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("topiccategory:update")
    public R update(@RequestBody TopicCategoryEntity topicCategory) {
        topicCategoryService.update(topicCategory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("topiccategory:delete")
    public R delete(@RequestBody Integer[] ids) {
        topicCategoryService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<TopicCategoryEntity> list = topicCategoryService.queryList(params);

        return R.ok().put("list", list);
    }
}
