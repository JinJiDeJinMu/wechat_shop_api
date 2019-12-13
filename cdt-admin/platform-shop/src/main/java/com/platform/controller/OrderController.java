package com.platform.controller;

import com.platform.constance.ShopShow;
import com.platform.entity.*;
import com.platform.service.ExpressOrderService;
import com.platform.service.OrderGoodsService;
import com.platform.service.OrderService;
import com.platform.service.ShippingService;
import com.platform.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-13 10:41:09
 */
@RestController
@RequestMapping("order")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
	@Autowired
	private OrderGoodsService orderGoodsService;
	@Autowired
	ShippingService shippingService;
	@Autowired
	private ExpressOrderService expressOrderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:list")
    public R list(@RequestParam Map<String, Object> params) {
        SysUserEntity sysUserEntity= ShiroUtils.getUserEntity();
        // 查询列表数据
        Query query = new Query(params);
        System.out.println("====="+query);
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchantId", sysUserEntity.getMerchantId());
        }
        List<OrderEntity> orderList = orderService.queryList(query);

        int total = orderService.queryTotal(query);
        HashMap<String,Object> hashMap = orderService.getTotalSum(params);
        for(OrderEntity user : orderList) {
        	user.setUserName(Base64.decode(user.getUserName()));
        }
        PageUtils pageUtil = new PageUtils(orderList, total, query.getLimit(), query.getPage());
        return R.ok(hashMap).put("page",pageUtil);
    }

    /**
     * 列表
     */
    @RequestMapping("/groupList")
    public R groupList(@RequestParam Map<String, Object> params) {
        SysUserEntity sysUserEntity= ShiroUtils.getUserEntity();
        // 查询列表数据
        Query query = new Query(params);
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchantId", sysUserEntity.getMerchantId());
        }
        List<GroupBuyingEntity> list = orderService.queryGroupList(query);
        int total = orderService.queryGroupTotal(query);
        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("order:info")
    public R info(@PathVariable("id") Integer id) {
        OrderEntity order = orderService.queryObject(id);
        Map<String, Object> params =new HashMap<String, Object>();
        params.put("orderId", order.getId());
        List<OrderGoodsEntity> list=  orderGoodsService.queryList(params);
        String goodsName="";
        //商品规格详情
      	String goodsSpecifitionNameValue="";
        for (OrderGoodsEntity orderGoodsEntity : list) {
        	goodsName=goodsName+orderGoodsEntity.getGoodsName()+",";
			if(orderGoodsEntity.getGoodsSpecifitionNameValue()!=null) {
				goodsSpecifitionNameValue=goodsSpecifitionNameValue+orderGoodsEntity.getGoodsSpecifitionNameValue()+",";
			}
		}
        order.setGoodsNames(goodsName);
        order.setGoodsSpecifitionNameValue(goodsSpecifitionNameValue);
        order.setUserName(Base64.decode(order.getUserName()));
        order.setAddress(order.getProvince() + order.getCity() + order.getDistrict() + order.getAddress());
        return R.ok().put("order", order);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/goodsinfo/{gid}")
    @RequiresPermissions("order:info")
    public R goodsinfo(@PathVariable("gid") Integer gid) {
    	OrderGoodsEntity orderGoods = orderGoodsService.queryObject(gid);
        return R.ok().put("orderGoods", orderGoods);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.update(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:delete")
    public R delete(@RequestBody Integer[] ids) {
        orderService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<OrderEntity> list = orderService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {
        int sum = orderService.queryTotal(params);

        return R.ok().put("sum", sum);
    }

    /**
     * 确定收货
     *
     * @param id
     * @return
     */
    @RequestMapping("/confirm")
    @RequiresPermissions("order:confirm")
    public R confirm(@RequestBody Integer id) {
        orderService.confirm(id);

        return R.ok();
    }

    /**
     * 发货
     *
     * @param order
     * @return
     */
    @RequestMapping("/sendGoods")
    @RequiresPermissions("order:sendGoods")
    public R sendGoods(@RequestBody OrderEntity order) {
        orderService.sendGoods(order);

        return R.ok();
    }


    /**
     * 查询订单状态为快递代收的详细信息
     * @param id
     * @return
     */
    @RequestMapping("/express/{id}")
    @RequiresPermissions("order:info")
    public R expressOrder(@PathVariable Integer id){
        ExpressOrderEntity expressOrderEntity = expressOrderService.queryOrderId(id);
        return R.ok().put("expressOrder",expressOrderEntity);
    }

}
