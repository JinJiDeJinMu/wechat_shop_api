package com.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.service.CdtDistridetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 用户购买分销得钱记录Controller
 *
 * @date 2019-12-19 23:46:09
 */
@RestController
@RequestMapping("cdtdistridetail")
public class CdtDistridetailController {
    @Autowired
    private CdtDistridetailService cdtDistridetailService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cdtdistridetail:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(), query.getLimit());

        QueryWrapper<CdtDistridetail> condition = new QueryWrapper<CdtDistridetail>();
        if (query.get("sidx") != null) {
            condition.orderByDesc(query.get("sidx").toString());
        }
        List<CdtDistridetail> cdtDistridetailList = cdtDistridetailService.list(condition);
        PageInfo pageInfo = new PageInfo(cdtDistridetailList);
        return R.ok().put("page", pageInfo);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("cdtdistridetail:info")
    @ResponseBody
    public R info(@PathVariable("id") Long id) {
        CdtDistridetail cdtDistridetail = cdtDistridetailService.getById(id);
        return R.ok().put("cdtDistridetail", cdtDistridetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cdtdistridetail:save")
    @ResponseBody
    public R save(@RequestBody CdtDistridetail cdtDistridetail) {
        Boolean result = cdtDistridetailService.save(cdtDistridetail);
        return result ? R.ok() : R.error("添加失败");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cdtdistridetail:update")
    @ResponseBody
    public R update(@RequestBody CdtDistridetail cdtDistridetail) {
        Boolean result = cdtDistridetailService.updateById(cdtDistridetail);
        return result ? R.ok() : R.error("更新失败");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cdtdistridetail:delete")
    @ResponseBody
    public R delete(@RequestBody Long[] ids) {
        Boolean result = cdtDistridetailService.removeByIds(Arrays.asList(ids));
        return result ? R.ok() : R.error("批量删除失败");
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<CdtDistridetail> list = cdtDistridetailService.listByMap(params);
        return R.ok().put("list", list);
    }
}
