package com.chundengtai.base.controller;

import com.chundengtai.base.entity.OrderGoodsEntity;
import com.chundengtai.base.service.admin.OrderGoodsService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ordergoods")
public class OrderGoodsController {
    @Autowired
    private OrderGoodsService orderGoodsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ordergoods:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<OrderGoodsEntity> orderGoodsList = orderGoodsService.queryList(query);
        int total = orderGoodsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(orderGoodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ordergoods:info")
    public R info(@PathVariable("id") Integer id) {
        OrderGoodsEntity orderGoods = orderGoodsService.queryObject(id);

        return R.ok().put("orderGoods", orderGoods);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ordergoods:save")
    public R save(@RequestBody OrderGoodsEntity orderGoods) {
        orderGoodsService.save(orderGoods);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ordergoods:update")
    public R update(@RequestBody OrderGoodsEntity orderGoods) {
        orderGoodsService.update(orderGoods);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ordergoods:delete")
    public R delete(@RequestBody Integer[] ids) {
        orderGoodsService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<OrderGoodsEntity> list = orderGoodsService.queryList(params);

        return R.ok().put("list", list);
    }
}
