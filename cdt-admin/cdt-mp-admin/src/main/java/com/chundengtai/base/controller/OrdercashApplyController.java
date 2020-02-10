package com.chundengtai.base.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.bean.User;
import com.chundengtai.base.constance.CashApplyENUM;
import com.chundengtai.base.constance.ShopShow;
import com.chundengtai.base.entity.*;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.service.admin.CdtMerchantService;
import com.chundengtai.base.service.admin.OrdercashApplyService;
import com.chundengtai.base.service.admin.UserService;
import com.chundengtai.base.utils.*;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.OrderType;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Controller
 *
 * @date 2019-12-11 11:29:38
 */
@RestController
@RequestMapping("ordercashapply")
public class OrdercashApplyController {

    @Autowired
    private OrdercashApplyService ordercashApplyService;

    @Autowired
    private CdtMerchantService cdtMerchantService;

    /*@Autowired
    private OrderService orderService;*/

    @Autowired
    private UserService userService;

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
    @RequestMapping("/save")
    @ResponseBody
    public R save() {

        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();

        if (ShiroUtils.getUserEntity().getMerchantId() == ShopShow.ADMINISTRATOR.getCode()) {
           return R.error(CashApplyENUM.NO_ADMIN_NO.getMsg());
        }
        CdtMerchantEntity cdtMerchantEntity = cdtMerchantService.queryObject(sysUserEntity.getMerchantId());
        if (null == cdtMerchantEntity || cdtMerchantEntity.getUserId() == null) {
            return R.error(CashApplyENUM.MERCHANT_NOOPEN_CASH.getMsg());
        }

        UserEntity user = userService.queryObject(cdtMerchantEntity.getUserId());
        List<Order> orderArrayList = orderService.list(new LambdaQueryWrapper<Order>()
        .in(Order::getOrderStatus, OrderStatusEnum.COMPLETED_ORDER,OrderStatusEnum.PINGLUN_ORDER)
        .eq(Order::getIsApply,0).eq(Order::getMerchantId,sysUserEntity.getMerchantId())
        .eq(Order::getPayStatus, PayTypeEnum.PAYED));
        if(orderArrayList != null){
            orderArrayList.forEach(e ->{
                long num = 7;
                long daysNum = Duration.between(DateTimeConvert.date2LocalDateTime(e.getConfirmTime()), LocalDateTime.now()).toDays();
                if (daysNum > num) {
                    OrdercashApplyEntity ordercashApplyEntity = new OrdercashApplyEntity();
                    ordercashApplyEntity.setOrderId(e.getId());
                    ordercashApplyEntity.setOrderSn(e.getOrderSn());
                    ordercashApplyEntity.setPayTime(e.getPayTime());
                    ordercashApplyEntity.setActualPrice(e.getActualPrice());
                    ordercashApplyEntity.setMerchantId(e.getMerchantId());
                    ordercashApplyEntity.setMerchantName(cdtMerchantEntity.getShopName());
                    ordercashApplyEntity.setApplyId(cdtMerchantEntity.getUserId().longValue());
                    ordercashApplyEntity.setApplyName(user.getRealName());
                    ordercashApplyEntity.setApplyTime(new Date());

                    ordercashApplyService.save(ordercashApplyEntity);

                }
            });
        }
        return R.error(CashApplyENUM.ORDER_CASH_APPLYSUCCESS.getMsg());
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
     *
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

        if (null == ordercashApplyEntity) {
            return R.ok(CashApplyENUM.ORDER_CASH_NOEXISTEN.getMsg());
        }

        //查询商家是否开通功能
        CdtMerchantEntity cdtMerchantEntity = cdtMerchantService.queryObject(ordercashApplyEntity.getMerchantId());
        if (cdtMerchantEntity.getCashStatus() != 1) {
            return R.ok(CashApplyENUM.MERCHANT_NOOPEN_CASH.getMsg());
        }

        //查询订单申请提现状态
        if (ordercashApplyEntity.getStatus() == 0) {
            ordercashApplyEntity.setEndTime(new Date());
            ordercashApplyEntity.setStatus(1);
            ordercashApplyEntity.setOperator(ShiroUtils.getUserEntity().getUserId());
            ordercashApplyEntity.setOperatorName(ShiroUtils.getUserEntity().getUsername());

            //微信转账
            UserEntity userEntity = userService.queryObject(cdtMerchantEntity.getUserId());
            Double money = ordercashApplyEntity.getActualPrice().doubleValue();
            if (ordercashApplyService.wechatMoneyToUser(userEntity, money)) {
                ordercashApplyService.update(ordercashApplyEntity);
                return R.ok(CashApplyENUM.ORDER_CASH_SUCCESS.getMsg());
            }
            return R.ok(CashApplyENUM.WHAT_PAY_ERROR.getMsg());
        }
        return R.ok(CashApplyENUM.ORDER_CASH_REBACK.getMsg());
    }

    @RequestMapping("/reject/{id}")
    public R reject(@PathVariable Integer id) {

        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            return R.ok(CashApplyENUM.NO_ADMIN_NOSHIRO.getMsg());
        }
        //判断订单是否已经审核
        OrdercashApplyEntity ordercashApplyEntity = ordercashApplyService.queryObject(id);

        if (null == ordercashApplyEntity) {
            return R.ok(CashApplyENUM.ORDER_CASH_NOEXISTEN.getMsg());
        }
        if (ordercashApplyEntity.getStatus() == 0) {
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
