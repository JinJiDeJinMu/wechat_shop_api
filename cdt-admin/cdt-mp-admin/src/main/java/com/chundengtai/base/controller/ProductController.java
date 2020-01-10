package com.chundengtai.base.controller;

import com.chundengtai.base.constance.ShopShow;
import com.chundengtai.base.entity.ProductEntity;
import com.chundengtai.base.service.admin.ProductService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @date 2017-08-30 14:31:21
 */
@RestController
@RequestMapping("product")
public class ProductController extends BaseController {
    @Autowired
    private ProductService productService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchant_id", ShiroUtils.getUserEntity().getMerchantId());
        }
        List<ProductEntity> productList = productService.queryList(query);
        int total = productService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(productList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("product:info")
    public R info(@PathVariable("id") Integer id) {
        ProductEntity product = productService.queryObject(id);
        return R.ok().put("product", product);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:save")
    public R save(@RequestBody ProductEntity product) {
        product.setMerchant_id(ShiroUtils.getUserEntity().getMerchantId());
        productService.save(product);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:update")
    public R update(@RequestBody ProductEntity product) {
        productService.update(product);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:delete")
    public R delete(@RequestBody Integer[] ids) {
        productService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     *
     * @param params
     * @return
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<ProductEntity> list = productService.queryList(params);
        return R.ok().put("list", list);
    }

    /**
     * 根据goodsId查询商品
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("/queryByGoodsId/{goodsId}")
    public R queryByGoodsId(@PathVariable("goodsId") String goodsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
        List<ProductEntity> list = productService.queryList(params);
        return R.ok().put("list", list);
    }
}
