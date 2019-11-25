package com.platform.api;


import com.chundengtai.base.result.Result;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.PurchasePeopleEntity;
import com.platform.service.ApiPurchasePeopleService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-11-25 09:39:01
 */
@Controller
@RequestMapping("purchasePeople")
public class ApiPurchasePeopleController {
    @Autowired
    private ApiPurchasePeopleService PurchasePeopleService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("nideshoppurchasepeople:list")
    @ResponseBody
    @IgnoreAuth
    public Result<Map<String, Object>> list(Integer goodsId) {
        //查询列表数据
        Map<String, Object> params= new HashMap<>();
        params.put("goodsId", goodsId);
        params.put("sidx", "id");
        params.put("order", "desc");
        List<PurchasePeopleEntity> nideshopPurchasePeopleList = PurchasePeopleService.queryList(params);
        Map<String, Object> PurchasePeopleList = new HashMap<String, Object>();
        PurchasePeopleList.put("purchasePeopleList",nideshopPurchasePeopleList);
        return Result.success(PurchasePeopleList);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("nideshoppurchasepeople:info")
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        PurchasePeopleEntity nideshopPurchasePeople = PurchasePeopleService.queryObject(id);

        return R.ok().put("nideshopPurchasePeople", nideshopPurchasePeople);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("nideshoppurchasepeople:save")
    @ResponseBody
    public R save(@RequestBody PurchasePeopleEntity nideshopPurchasePeople) {
        PurchasePeopleService.save(nideshopPurchasePeople);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("nideshoppurchasepeople:update")
    @ResponseBody
    public R update(@RequestBody PurchasePeopleEntity nideshopPurchasePeople) {
        PurchasePeopleService.update(nideshopPurchasePeople);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("nideshoppurchasepeople:delete")
    @ResponseBody
    public R delete(@RequestBody Integer[] ids) {
        PurchasePeopleService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<PurchasePeopleEntity> list = PurchasePeopleService.queryList(params);

        return R.ok().put("list", list);
    }
}
