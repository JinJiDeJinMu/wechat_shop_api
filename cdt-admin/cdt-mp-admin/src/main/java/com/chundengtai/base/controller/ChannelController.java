package com.chundengtai.base.controller;

import com.chundengtai.base.entity.ChannelEntity;
import com.chundengtai.base.service.admin.ChannelService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 */
@RestController
@RequestMapping("channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("channel:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ChannelEntity> channelList = channelService.queryList(query);
        int total = channelService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(channelList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("channel:info")
    public R info(@PathVariable("id") Integer id) {
        ChannelEntity channel = channelService.queryObject(id);

        return R.ok().put("channel", channel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("channel:save")
    public R save(@RequestBody ChannelEntity channel) {
        channelService.save(channel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("channel:update")
    public R update(@RequestBody ChannelEntity channel) {
        channelService.update(channel);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("channel:delete")
    public R delete(@RequestBody Integer[] ids) {
        channelService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ChannelEntity> list = channelService.queryList(params);

        return R.ok().put("list", list);
    }
}
