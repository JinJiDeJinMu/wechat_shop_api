package com.chundengtai.base.controller;

import com.chundengtai.base.entity.CollectEntity;
import com.chundengtai.base.service.admin.CollectService;
import com.chundengtai.base.utils.Base64;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("collect")
public class CollectController {
    @Autowired
    private CollectService collectService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("collect:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<CollectEntity> collectList = collectService.queryList(query);
        int total = collectService.queryTotal(query);
        for (CollectEntity user : collectList) {
            user.setUserName(Base64.decode(user.getUserName()));
        }
        PageUtils pageUtil = new PageUtils(collectList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("collect:info")
    public R info(@PathVariable("id") Integer id) {
        CollectEntity collect = collectService.queryObject(id);

        return R.ok().put("collect", collect);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("collect:save")
    public R save(@RequestBody CollectEntity collect) {
        collectService.save(collect);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("collect:update")
    public R update(@RequestBody CollectEntity collect) {
        collectService.update(collect);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("collect:delete")
    public R delete(@RequestBody Integer[] ids) {
        collectService.deleteBatch(ids);

        return R.ok();
    }

}
