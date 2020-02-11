package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.bean.OrderApply;
import com.chundengtai.base.constance.CashApplyENUM;
import com.chundengtai.base.constance.ShopShow;
import com.chundengtai.base.entity.CdtMerchantEntity;
import com.chundengtai.base.entity.OrdercashApplyEntity;
import com.chundengtai.base.entity.SysUserEntity;
import com.chundengtai.base.entity.UserEntity;
import com.chundengtai.base.service.OrderApplyService;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.service.admin.ApplyCashService;
import com.chundengtai.base.service.admin.CdtMerchantService;
import com.chundengtai.base.service.admin.UserService;
import com.chundengtai.base.transfer.BaseForm;
import com.chundengtai.base.utils.DateTimeConvert;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.ShiroUtils;
import com.chundengtai.base.weixinapi.DistributionStatus;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orderApply")
public class OrderApplyController {
    @Autowired
    private OrderApplyService orderApplyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CdtMerchantService cdtMerchantService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplyCashService applyCashService;

    /**
     * 列表
     */
    @PostMapping("/list.json")
    public R list(@RequestBody BaseForm<OrderApply> params) {
        QueryWrapper<OrderApply> conditon = new QueryWrapper<>();
        if (!StringUtils.isEmpty(params.getSortField()) && !StringUtils.isEmpty(params.getOrder())) {
            if (params.getOrder().equalsIgnoreCase("asc")) {
                conditon.orderByAsc(params.getSortField());
            } else {
                conditon.orderByDesc(params.getSortField());
            }
        }
        //Integer=====
        if (params.getData().getId() != null) {
            conditon.eq("id", params.getData().getId());
        }
        //String=====
        if (params.getData().getOrderSn() != null) {
            conditon.eq("order_sn", params.getData().getOrderSn());
        }
        //Long=====
        if (params.getData().getMerchantId() != null) {
            conditon.eq("merchant_id", params.getData().getMerchantId());
        }
        //String=====
        if (params.getData().getMerchantName() != null) {
            conditon.eq("merchant_name", params.getData().getMerchantName());
        }
        //BigDecimal=====
        if (params.getData().getMoney() != null) {
            conditon.eq("money", params.getData().getMoney());
        }
        //Long=====
        if (params.getData().getUserId() != null) {
            conditon.eq("user_id", params.getData().getUserId());
        }
        //String=====
        if (params.getData().getWxopenId() != null) {
            conditon.eq("wxopen_id", params.getData().getWxopenId());
        }
        //String=====
        if (params.getData().getUserName() != null) {
            conditon.eq("user_name", params.getData().getUserName());
        }
        //Date=====
        //Date=====
        //Integer=====
        if (params.getData().getStatus() != null) {
            conditon.eq("status", params.getData().getStatus());
        }
        //String=====
        if (params.getData().getRemarks() != null) {
            conditon.eq("remarks", params.getData().getRemarks());
        }
        PageHelper.startPage(params.getPageIndex(), params.getPageSize());
        List<OrderApply> collectList = orderApplyService.list(conditon);
        PageInfo pageInfo = new PageInfo(collectList);
        return R.ok(pageInfo);
    }

    /**
     * 信息
     */
    @GetMapping("/getModel/{id}.json")
    public R info(@PathVariable("id") Integer id) {
        OrderApply model = orderApplyService.getById(id);
        return R.ok(model);
    }

    /**
     * 保存
     */
    @GetMapping("/saveModel.json")
    @Transactional
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
                .in(Order::getOrderStatus, OrderStatusEnum.COMPLETED_ORDER, OrderStatusEnum.PINGLUN_ORDER)
                .eq(Order::getIsApply, 0).eq(Order::getMerchantId, sysUserEntity.getMerchantId())
                .eq(Order::getPayStatus, PayTypeEnum.PAYED));
        if (orderArrayList != null) {
            orderArrayList.forEach(e -> {
                long num = 7;
                long daysNum = Duration.between(DateTimeConvert.date2LocalDateTime(e.getConfirmTime()), LocalDateTime.now()).toDays();
                if (daysNum > num) {
                    OrderApply orderApply = new OrderApply();
                    orderApply.setId(e.getId());
                    orderApply.setOrderSn(e.getOrderSn());
                    orderApply.setMerchantId(e.getMerchantId());
                    orderApply.setMerchantName(cdtMerchantEntity.getShopName());
                    orderApply.setUserId(cdtMerchantEntity.getUserId().longValue());
                    orderApply.setUserName(user.getRealName());
                    orderApply.setApplyTime(new Date());
                    orderApply.setMoney(e.getActualPrice());
                    orderApply.setWxopenId(user.getWeixinOpenid());
                    boolean flag = orderApplyService.save(orderApply);
                    if(flag){
                        e.setIsApply(1);
                        orderService.updateById(e);
                    }
                }
            });
        }
        return R.error(CashApplyENUM.ORDER_CASH_APPLYSUCCESS.getMsg());
    }

    /**
     * 修改
     */
    @PostMapping("/updateModel.json")
    public R update(@RequestBody OrderApply paramModel) {
        boolean result = orderApplyService.updateById(paramModel);
        return R.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteModel.json")
    public R delete(@RequestBody Integer[] ids) {
        boolean result = orderApplyService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
    }

    @RequestMapping("/reviewModel.json")
    @Transactional
    public R review(@RequestBody Integer[] ids) {
        List<OrderApply> orderApplies = orderApplyService.listByIds(Arrays.asList(ids));
        orderApplies = orderApplies.stream().filter(e -> e.getStatus() == 0 && e.getStatus() ==2).collect(Collectors.toList());
        if (orderApplies != null) {
            orderApplies.forEach(e ->{
                String weixinOpenid = e.getWxopenId();
                String realName = e.getUserName();
                double money = e.getMoney().doubleValue();
                R result = applyCashService.wechatMoneyToUser(weixinOpenid, realName, money);
                if (result.get("code").equals(0)) {
                    try {
                        //更新分销审核表类型
                        e.setStatus(1);
                        e.setEndTime(new Date());
                        //更新订单类型
                        Order order = orderService.getById(e.getId());
                        //2代表审核通过
                        order.setIsApply(2);
                        orderService.updateById(order);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    e.setStatus(2);
                    e.setRemarks(result.get("msg").toString());
                }
                orderApplyService.updateById(e);
            });
        }
        return R.ok();
     }
    }
