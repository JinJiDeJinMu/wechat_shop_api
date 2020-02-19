package com.chundengtai.base.controller;

import com.chundengtai.base.bean.School;
import com.chundengtai.base.constance.ShopShow;
import com.chundengtai.base.dao.GoodsMapper;
import com.chundengtai.base.entity.GoodsEntity;
import com.chundengtai.base.entity.GoodsGalleryEntity;
import com.chundengtai.base.entity.SysUserEntity;
import com.chundengtai.base.service.SchoolService;
import com.chundengtai.base.service.admin.GoodsService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
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
        if(goods.getGoodsImgList().size() >0) {
            GoodsGalleryEntity goodsGalleryEntity = goods.getGoodsImgList().get(0);
            String imgUrl = goodsGalleryEntity.getImgUrl();
            goods.setPrimaryPicUrl(imgUrl);
        }
        goods.setBrowse(1);
        goods.setMerchantId(-1L);
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
     * 批量上架
     */
    @RequestMapping("/enSale")
    public R enSale(@RequestBody Integer[] ids) {
        try {
            for (int i = 0; i < ids.length; i++) {
                goodsService.enSale(ids[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok();
    }

    /**
     * 批量下架
     */
    @RequestMapping("/unSale")
    public R unSale(@RequestBody Integer[] ids) {
        try {
            for (int i = 0; i < ids.length; i++) {
                goodsService.unSale(ids[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok();
    }

    @RequestMapping("/en")
    public R en(@RequestBody Integer id){
        goodsService.enSale(id);
        return R.ok();
    }

    @RequestMapping("/un")
    public R un(@RequestBody Integer id){
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
