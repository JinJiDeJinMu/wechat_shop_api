package com.platform.controller;


import com.platform.entity.CdtNewsBulletinEntity;
import com.platform.service.CdtNewsBulletinService;
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
 * 消息公告Controller
 *
 * @author lipengjun
 * @date 2019-11-25 11:38:00
 */
@Controller
@RequestMapping("cdtnewsbulletin")
public class CdtNewsBulletinController {
    @Autowired
    private CdtNewsBulletinService cdtNewsBulletinService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("cdtnewsbulletin:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<CdtNewsBulletinEntity> cdtNewsBulletinList = cdtNewsBulletinService.queryList(query);
        int total = cdtNewsBulletinService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(cdtNewsBulletinList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("cdtnewsbulletin:info")
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        CdtNewsBulletinEntity cdtNewsBulletin = cdtNewsBulletinService.queryObject(id);

        return R.ok().put("cdtNewsBulletin", cdtNewsBulletin);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("cdtnewsbulletin:save")
    @ResponseBody
    public R save(@RequestBody CdtNewsBulletinEntity cdtNewsBulletin) {
        cdtNewsBulletinService.save(cdtNewsBulletin);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("cdtnewsbulletin:update")
    @ResponseBody
    public R update(@RequestBody CdtNewsBulletinEntity cdtNewsBulletin) {
        cdtNewsBulletinService.update(cdtNewsBulletin);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("cdtnewsbulletin:delete")
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
