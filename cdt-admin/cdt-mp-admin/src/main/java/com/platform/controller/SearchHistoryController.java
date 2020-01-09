package com.platform.controller;

import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.platform.entity.SearchHistoryEntity;
import com.platform.service.SearchHistoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @date 2017-08-13 10:41:10
 */
@RestController
@RequestMapping("searchhistory")
public class SearchHistoryController {
    @Autowired
    private SearchHistoryService searchHistoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("searchhistory:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<SearchHistoryEntity> searchHistoryList = searchHistoryService.queryList(query);
        int total = searchHistoryService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(searchHistoryList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("searchhistory:info")
    public R info(@PathVariable("id") Integer id) {
        SearchHistoryEntity searchHistory = searchHistoryService.queryObject(id);

        return R.ok().put("searchHistory", searchHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("searchhistory:save")
    public R save(@RequestBody SearchHistoryEntity searchHistory) {
        searchHistoryService.save(searchHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("searchhistory:update")
    public R update(@RequestBody SearchHistoryEntity searchHistory) {
        searchHistoryService.update(searchHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("searchhistory:delete")
    public R delete(@RequestBody Integer[] ids) {
        searchHistoryService.deleteBatch(ids);

        return R.ok();
    }

}
