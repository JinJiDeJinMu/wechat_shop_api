package com.chundengtai.base.controller;


import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.entity.CdtNewsBulletinEntity;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.ApiCdtNewsBulletinService;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 消息公告Controller
 */
@Controller
@RequestMapping("api/newsBulletin")
public class ApiCdtNewsBulletinController {
    @Autowired
    private ApiCdtNewsBulletinService cdtNewsBulletinService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("cdtnewsbulletin:list")
    @IgnoreAuth
    @ResponseBody
    public Result<Map<String, Object>> list() {
        //查询列表数据
        Map<String, Object> params = new HashMap<>();

        List<CdtNewsBulletinEntity> cdtNewsBulletinList = cdtNewsBulletinService.queryList(params);

        Map<String, Object> cdtNewsBulletinLists = new HashMap<String, Object>();
        cdtNewsBulletinLists.put("purchasePeopleList", cdtNewsBulletinList);
        return Result.success(cdtNewsBulletinLists);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("cdtnewsbulletin:info")
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        CdtNewsBulletinEntity cdtNewsBulletin = cdtNewsBulletinService.queryObject(id);

        return R.ok().put("cdtNewsBulletin", cdtNewsBulletin);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cdtnewsbulletin:save")
    @ResponseBody
    public R save(@RequestBody CdtNewsBulletinEntity cdtNewsBulletin) {
        cdtNewsBulletinService.save(cdtNewsBulletin);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cdtnewsbulletin:update")
    @ResponseBody
    public R update(@RequestBody CdtNewsBulletinEntity cdtNewsBulletin) {
        cdtNewsBulletinService.update(cdtNewsBulletin);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cdtnewsbulletin:delete")
    @ResponseBody
    public R delete(@RequestBody Integer[] ids) {
        cdtNewsBulletinService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<CdtNewsBulletinEntity> list = cdtNewsBulletinService.queryList(params);

        return R.ok().put("list", list);
    }
}
