package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.chundengtai.base.bean.CdtCoupon;
import com.chundengtai.base.bean.dto.CdtCouponDao;
import com.chundengtai.base.entity.SysUserEntity;
import com.chundengtai.base.service.CdtCouponService;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.ShiroUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 优惠券Controller
 */
@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private CdtCouponService cdtCouponService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("coupon:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Query query = new Query(params);
        query.put("merchantId", sysUserEntity.getMerchantId());
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<CdtCoupon> couponList = cdtCouponService.list();
        couponList = couponList.stream().sorted(Comparator.comparing(CdtCoupon::getId).reversed()).collect(Collectors.toList());
        PageUtils pageUtil = new PageUtils(new PageInfo(couponList));

        return R.ok().put("page", pageUtil);
    }


    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("coupon:info")
    public R info(@PathVariable("id") Integer id) {
        CdtCoupon coupon = cdtCouponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("coupon:save")
    public R save(@RequestBody CdtCouponDao couponDao) {

        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        couponDao.setMerchantId(sysUserEntity.getMerchantId().intValue());
        System.out.println("==="+couponDao);
        cdtCouponService.saveCoupon(couponDao);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("coupon:update")
    public R update(@RequestBody CdtCoupon coupon) {
        cdtCouponService.saveOrUpdate(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("coupon:delete")
    public R delete(@RequestBody Integer[] ids) {
        cdtCouponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        params.put("merchantId", sysUserEntity.getMerchantId());
        List<CdtCoupon> list = cdtCouponService.listByMap(params);

        return R.ok().put("list", list);
    }

   /* *//**
     * 按用户、商品下发优惠券
     *
     * @param params
     * @return
     *//*
    @RequiresPermissions("coupon:publish")
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    public R publish(@RequestBody Map<String, Object> params) {
        return couponService.publish(params);
    }*/
}
