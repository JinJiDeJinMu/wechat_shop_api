package com.platform.controller;


import com.platform.constance.CashApplyENUM;
import com.platform.constance.ShopShow;
import com.platform.entity.CdtMerchantEntity;
import com.platform.entity.OrderEntity;
import com.platform.entity.OrdercashApplyEntity;
import com.platform.service.CdtMerchantService;
import com.platform.service.OrderService;
import com.platform.service.OrdercashApplyService;
import com.platform.utils.*;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-12-11 11:29:38
 */
@RestController
@RequestMapping("ordercashapply")
public class OrdercashApplyController {

    @Autowired
    private OrdercashApplyService ordercashApplyService;

    @Autowired
    private CdtMerchantService cdtMerchantService;

    @Autowired
    private OrderService orderService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ordercashapply:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {

        //查询列表数据
        Query query = new Query(params);
        List<OrdercashApplyEntity> ordercashApplyList = ordercashApplyService.queryList(params);
        int total = ordercashApplyService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(ordercashApplyList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ordercashapply:info")
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        OrdercashApplyEntity ordercashApply = ordercashApplyService.queryObject(id);

        return R.ok().put("ordercashApply", ordercashApply);
    }

    /**
     * 保存
     */
    @RequestMapping("/save/{id}")
    @RequiresPermissions("ordercashapply:save")
    @ResponseBody
    public R save(@PathVariable Integer id) {

        OrdercashApplyEntity ordercashApply = ordercashApplyService.queryObject(id);
        if(null != ordercashApply){
            return R.error(CashApplyENUM.ORDER_CASH_EXISTEN.getMsg());
        }

        OrderEntity orderEntity = orderService.queryObject(id);
        if(orderEntity == null || orderEntity.getMerchantId() == null){
            return R.error(CashApplyENUM.ORDER_CASH_NOEXISTEN.getMsg());
        }

        if(orderEntity.getMerchantId() <0){
            return R.error(CashApplyENUM.NO_ADMIN_NO.getMsg());
        }
        //查询商家是否开通功能
        CdtMerchantEntity cdtMerchantEntity = cdtMerchantService.queryObject(orderEntity.getMerchantId());
        if(null==cdtMerchantEntity || cdtMerchantEntity.getCashStatus() != 1){
            return R.error(CashApplyENUM.MERCHANT_NOOPEN_CASH.getMsg());
        }

        if(orderEntity.getPayStatus() == 2 && orderEntity.getOrderStatus() == 402){
            OrdercashApplyEntity ordercashApplyEntity = new OrdercashApplyEntity();
            ordercashApplyEntity.setOrderId(id);
            ordercashApplyEntity.setOrderSn(orderEntity.getOrderSn());
            ordercashApplyEntity.setPayTime(orderEntity.getPayTime());
            ordercashApplyEntity.setActualPrice(orderEntity.getActualPrice());
            ordercashApplyEntity.setMerchantId(orderEntity.getMerchantId());
            ordercashApplyEntity.setMerchantName(cdtMerchantEntity.getShopName());
            ordercashApplyEntity.setApplyId(ShiroUtils.getUserEntity().getUserId());
            ordercashApplyEntity.setApplyName(ShiroUtils.getUserEntity().getUsername());
            ordercashApplyEntity.setApplyTime(new Date());

            ordercashApplyService.save(ordercashApplyEntity);
            return R.ok(CashApplyENUM.ORDER_CASH_APPLYSUCCESS.getMsg());

        }
        return R.error(CashApplyENUM.ORDER_CASH_NOAPPLY.getMsg());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ordercashapply:update")
    @ResponseBody
    public R update(@RequestBody OrdercashApplyEntity ordercashApply) {
        ordercashApplyService.update(ordercashApply);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ordercashapply:delete")
    @ResponseBody
    public R delete(@RequestBody Integer[] ids) {
        ordercashApplyService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<OrdercashApplyEntity> list = ordercashApplyService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 审核提现
     * @param id
     * @return
     */
    @RequestMapping("/review/{id}")
    @Transactional
    public R review(@PathVariable("id") Integer id) {

            if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
                return R.ok(CashApplyENUM.NO_ADMIN_NOSHIRO.getMsg());
            }
           //判断订单是否已存在
            OrdercashApplyEntity ordercashApplyEntity = ordercashApplyService.queryObject(id);

            if(null == ordercashApplyEntity){
                return R.ok(CashApplyENUM.ORDER_CASH_NOEXISTEN.getMsg());
            }

            //查询商家是否开通功能
            CdtMerchantEntity cdtMerchantEntity = cdtMerchantService.queryObject(ordercashApplyEntity.getMerchantId());
            if(cdtMerchantEntity.getCashStatus() != 1){
                return R.ok(CashApplyENUM.MERCHANT_NOOPEN_CASH.getMsg());
            }

            //查询订单申请提现状态
            if(ordercashApplyEntity.getStatus() == 0){

                ordercashApplyEntity.setEndTime(new Date());
                ordercashApplyEntity.setStatus(1);
                ordercashApplyEntity.setOperator(ShiroUtils.getUserEntity().getMerchantId());
                ordercashApplyEntity.setOperatorName(ShiroUtils.getUserEntity().getUsername());
                //修改提现订单申请状态
                ordercashApplyService.update(ordercashApplyEntity);
                //微信转账
                return R.ok(CashApplyENUM.ORDER_CASH_SUCCESS.getMsg());

            }
            return R.ok(CashApplyENUM.ORDER_CASH_REBACK.getMsg());
    }

    @RequestMapping("/reject/{id}")
    public R reject(@PathVariable Integer id){

        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            return R.ok(CashApplyENUM.NO_ADMIN_NOSHIRO.getMsg());
        }
        //判断订单是否已经审核
        OrdercashApplyEntity ordercashApplyEntity = ordercashApplyService.queryObject(id);

        if(null == ordercashApplyEntity){
            return R.ok(CashApplyENUM.ORDER_CASH_NOEXISTEN.getMsg());
        }
        if(ordercashApplyEntity.getStatus() ==0){
            ordercashApplyEntity.setStatus(2);
            ordercashApplyEntity.setEndTime(new Date());
            ordercashApplyEntity.setOperator(ShiroUtils.getUserEntity().getUserId());
            ordercashApplyEntity.setOperatorName(ShiroUtils.getUserEntity().getUsername());

            ordercashApplyService.update(ordercashApplyEntity);
            return R.ok(CashApplyENUM.ORDER_CASH_REJECT.getMsg());
        }
        return R.ok(CashApplyENUM.ORDER_CASH_NOREJECT.getMsg());
    }

}
