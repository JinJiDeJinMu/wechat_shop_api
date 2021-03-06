package com.chundengtai.base.controller;

import com.chundengtai.base.entity.GoodsAttributeEntity;
import com.chundengtai.base.service.admin.GoodsAttributeService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("goodsattribute")
public class GoodsAttributeController {
    @Autowired
    private GoodsAttributeService goodsAttributeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goodsattribute:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<GoodsAttributeEntity> goodsAttributeList = goodsAttributeService.queryList(query);
        int total = goodsAttributeService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsAttributeList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goodsattribute:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsAttributeEntity goodsAttribute = goodsAttributeService.queryObject(id);

        return R.ok().put("goodsAttribute", goodsAttribute);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goodsattribute:save")
    public R save(@RequestBody GoodsAttributeEntity goodsAttribute) {
        goodsAttributeService.save(goodsAttribute);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goodsattribute:update")
    public R update(@RequestBody GoodsAttributeEntity goodsAttribute) {
        goodsAttributeService.update(goodsAttribute);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goodsattribute:delete")
    public R delete(@RequestBody Integer[] ids) {
        goodsAttributeService.deleteBatch(ids);

        return R.ok();
    }

}
