package com.platform.controller;

import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.ShiroUtils;
import com.platform.constance.ShopShow;
import com.platform.entity.GoodsIssueEntity;
import com.platform.service.GoodsIssueService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @date 2017-08-23 14:12:34
 */
@RestController
@RequestMapping("goodsissue")
public class GoodsIssueController extends BaseController {
    @Autowired
    private GoodsIssueService goodsIssueService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goodsissue:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = getqCurrentQuery(params);
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchantId", ShiroUtils.getUserEntity().getMerchantId());
        }
        List<GoodsIssueEntity> goodsIssueList = goodsIssueService.queryList(query);
        int total = goodsIssueService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsIssueList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goodsissue:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsIssueEntity goodsIssue = goodsIssueService.queryObject(id);

        return R.ok().put("goodsIssue", goodsIssue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goodsissue:save")
    public R save(@RequestBody GoodsIssueEntity goodsIssue) {
        goodsIssue.setMerchant_id(ShiroUtils.getUserEntity().getMerchantId().intValue());
        goodsIssueService.save(goodsIssue);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goodsissue:update")
    public R update(@RequestBody GoodsIssueEntity goodsIssue) {
        goodsIssueService.update(goodsIssue);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goodsissue:delete")
    public R delete(@RequestBody Integer[] ids) {
        goodsIssueService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<GoodsIssueEntity> list = goodsIssueService.queryList(params);

        return R.ok().put("list", list);
    }
}
