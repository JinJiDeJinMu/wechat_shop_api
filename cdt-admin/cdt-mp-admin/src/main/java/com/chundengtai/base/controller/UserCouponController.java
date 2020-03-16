package com.chundengtai.base.controller;

import com.chundengtai.base.bean.dto.CdtUserCouponDao;
import com.chundengtai.base.constance.ShopShow;
import com.chundengtai.base.entity.SysUserEntity;
import com.chundengtai.base.entity.UserCouponEntity;
import com.chundengtai.base.service.CdtUserCouponService;
import com.chundengtai.base.service.admin.UserCouponService;
import com.chundengtai.base.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("usercoupon")
public class UserCouponController {

    @Autowired
    private CdtUserCouponService cdtUserCouponService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("usercoupon:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Query query = new Query(params);
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchantId", sysUserEntity.getMerchantId());
        }
        List<CdtUserCouponDao> userCouponList = cdtUserCouponService.getUserCounponList();
        int total = (int)userCouponList.stream().count();

        userCouponList.stream().forEach(e ->{
            e.setNickName(Base64.decode(e.getNickName()));
        });
        PageUtils pageUtil = new PageUtils(userCouponList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

   /* *//**
     * 查看信息
     *//*
    @RequestMapping("/info/{id}")
    @RequiresPermissions("usercoupon:info")
    public R info(@PathVariable("id") Integer id) {
        CdtUserCouponDao userCoupon = cdtUserCouponService.getById(id);

        return R.ok().put("userCoupon", userCoupon);
    }

    *//**
     * 保存
     *//*
    @RequestMapping("/save")
    @RequiresPermissions("usercoupon:save")
    public R save(@RequestBody UserCouponEntity userCoupon) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        userCoupon.setMerchantId(sysUserEntity.getMerchantId());
        userCouponService.save(userCoupon);

        return R.ok();
    }

    *//**
     * 修改
     *//*
    @RequestMapping("/update")
    @RequiresPermissions("usercoupon:update")
    public R update(@RequestBody UserCouponEntity userCoupon) {
        userCouponService.update(userCoupon);

        return R.ok();
    }

    *//**
     * 删除
     *//*
    @RequestMapping("/delete")
    @RequiresPermissions("usercoupon:delete")
    public R delete(@RequestBody Integer[] ids) {
        userCouponService.deleteBatch(ids);

        return R.ok();
    }

    *//**
     * 查看所有列表
     *//*
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<CdtUserCouponDao> list = cdtUserCouponService.list();

        return R.ok().put("list", list);
    }*/
}
