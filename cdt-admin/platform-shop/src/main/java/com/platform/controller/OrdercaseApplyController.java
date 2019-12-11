package com.platform.controller;


import com.platform.constance.ShopShow;
import com.platform.entity.OrdercaseApplyEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.OrdercaseApplyService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author hujibiao
 * @date 2019-12-10 17:30:30
 */
@RestController
@RequestMapping("OrdercaseApply")
public class OrdercaseApplyController {

    @Autowired
    private OrdercaseApplyService ordercaseApplyService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<OrdercaseApplyEntity> OrdercaseApplyList = ordercaseApplyService.queryList(query);
        int total = ordercaseApplyService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(OrdercaseApplyList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    @RequestMapping("/test")
    @ResponseBody
    public R t(){
        return R.ok();
    }
    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("OrdercaseApply:info")
    public R info(@PathVariable("id") Integer id) {
        OrdercaseApplyEntity OrdercaseApply = ordercaseApplyService.queryObject(id);

        return R.ok().put("OrdercaseApply", OrdercaseApply);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("OrdercaseApply:save")
    public R save(@RequestBody OrdercaseApplyEntity OrdercaseApply) {
        ordercaseApplyService.save(OrdercaseApply);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("OrdercaseApply:update")
    public R update(@RequestBody OrdercaseApplyEntity OrdercaseApply) {
        ordercaseApplyService.update(OrdercaseApply);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("OrdercaseApply:delete")
    public R delete(@RequestBody Integer[] ids) {
        ordercaseApplyService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<OrdercaseApplyEntity> list = ordercaseApplyService.queryList(params);

        return R.ok().put("list", list);
    }

    @RequestMapping("/review/{id}/{status}")
    public R Review(@PathVariable Integer id,@PathVariable Integer status){

        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            return R.error("不是超级管理员，没有权限审核！");
        }

        return R.ok();
    }
}
