package com.platform.controller;

import com.chundengtai.base.bean.School;
import com.chundengtai.base.service.SchoolService;
import com.platform.constance.ShopShow;
import com.platform.entity.GoodsEntity;
import com.platform.entity.SysUserEntity;
import com.platform.service.GoodsService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @date 2017-08-21 21:19:49
 */
@RestController
@RequestMapping("goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SchoolService schoolService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goods:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        if (ShiroUtils.getUserEntity().getMerchantId() == null) {
            return R.ok().put("list", null);
        }
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchantId", ShiroUtils.getUserEntity().getMerchantId());
        }
        query.put("isDelete", 0);
        List<GoodsEntity> goodsList = goodsService.queryList(query);
        int total = goodsService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goods:info")
    public R info(@PathVariable("id") Integer id) {
        GoodsEntity goods = goodsService.queryObject(id);

        return R.ok().put("goods", goods);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goods:save")
    public R save(@RequestBody GoodsEntity goods) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        if (sysUserEntity.getCreateUserId().intValue() != ShopShow.ADMINISTRATOR.getCode()) {
            goods.setMerchantId(sysUserEntity.getMerchantId());
        }
        goods.setBrowse(1);
        System.out.println("1111" + goods);
        goodsService.save(goods);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goods:update")
    public R update(@RequestBody GoodsEntity goods) {
        goodsService.update(goods);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goods:delete")
    public R delete(@RequestBody Integer[] ids) {
        goodsService.deleteBatch(ids);
        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {
        params.put("isDelete", 0);
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            params.put("merchantId", ShiroUtils.getUserEntity().getMerchantId());
        }
        List<GoodsEntity> list = goodsService.queryList(params);
        return R.ok().put("list", list);
    }


    /**
     * 商品回收站
     *
     * @param params
     * @return
     */
    @RequestMapping("/historyList")
    public R historyList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Query query = new Query(params);

        if (ShiroUtils.getUserEntity().getMerchantId().intValue() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchantId", sysUserEntity.getMerchantId());
        }
        query.put("isDelete", 1);
        List<GoodsEntity> goodsList = goodsService.queryList(query);

        int total = goodsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 商品从回收站恢复
     */
    @RequestMapping("/back")
    @RequiresPermissions("goods:back")
    public R back(@RequestBody Integer[] ids) {
        goodsService.back(ids);

        return R.ok();
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {

        params.put("isDelete", 0);
        int sum = goodsService.queryTotal(params);
        return R.ok().put("goodsSum", sum);
    }

    /**
     * 上架
     */
    @RequestMapping("/enSale")
    public R enSale(@RequestBody Integer id) {
        goodsService.enSale(id);

        return R.ok();
    }

    /**
     * 下架
     */
    @RequestMapping("/unSale")
    public R unSale(@RequestBody Integer id) {
        goodsService.unSale(id);

        return R.ok();
    }

    /**
     * 查询所有学校信息
     *
     * @return
     */
    @GetMapping("/schoollist")
    public R queryList() {
        List<School> list = schoolService.list();
        return R.ok().put("list", list);
    }
}