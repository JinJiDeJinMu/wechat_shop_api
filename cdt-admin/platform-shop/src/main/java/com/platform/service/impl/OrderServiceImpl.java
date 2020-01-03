package com.platform.service.impl;

import com.platform.dao.OrderDao;
import com.platform.dao.ShippingDao;
import com.platform.entity.GroupBuyingEntity;
import com.platform.entity.OrderEntity;
import com.platform.entity.ShippingEntity;
import com.platform.service.OrderService;
import com.platform.utils.RRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ShippingDao shippingDao;

    @Override
    public OrderEntity queryObject(Integer id) {
        return orderDao.queryObject(id);
    }

    @Override
    public List<OrderEntity> queryList(Map<String, Object> map) {
        return orderDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return orderDao.queryTotal(map);
    }

    @Override
    public int save(OrderEntity order) {
        return orderDao.save(order);
    }

    @Override
    public int update(OrderEntity order) {
        return orderDao.update(order);
    }

    @Override
    public int delete(Integer id) {
        return orderDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return orderDao.deleteBatch(ids);
    }

    @Override
    public int confirm(Integer id) {
        OrderEntity orderEntity = queryObject(id);
        Integer shippingStatus = orderEntity.getShippingStatus();//发货状态
        Integer payStatus = orderEntity.getPayStatus();//付款状态
        if (2 != payStatus) {
            throw new RRException("此订单未付款，不能确认收货！");
        }
        if (4 == shippingStatus) {
            throw new RRException("此订单处于退货状态，不能确认收货！");
        }
        if (0 == shippingStatus) {
            throw new RRException("此订单未发货，不能确认收货！");
        }
        orderEntity.setShippingStatus(2);
        orderEntity.setOrderStatus(301);
        orderEntity.setConfirmTime(new Date());
        return orderDao.update(orderEntity);
    }

    @Override
    public int sendGoods(OrderEntity order) {
        Integer payStatus = order.getPayStatus();//付款状态
        if (2 != payStatus) {
            throw new RRException("此订单未付款！");
        }

        order = orderDao.queryObject(order.getId());
       /* if (order.getGoodsType().equals(GoodsTypeEnum.WRITEOFF_ORDER.getCode())
                || order.getGoodsType().equals(GoodsTypeEnum.EXPRESS_GET.getCode())) {
            throw new RRException("此订单不是普通订单不能做发货操作！");
        }*/
        ShippingEntity shippingEntity = shippingDao.queryObject(order.getShippingId());
        if (null != shippingEntity) {
            order.setShippingName(shippingEntity.getName());
        }

        order.setOrderStatus(207);//订单待收货
        order.setShippingStatus(1);//已发货
        return orderDao.update(order);
    }

    @Override
    public HashMap<String, Object> getTotalSum(Map<String, Object> map) {
        return orderDao.getTotalSum(map);
    }


    @Override
    public List<GroupBuyingEntity> queryGroupList(Map<String, Object> map) {
        return orderDao.queryGroupList(map);
    }
    @Override
    public int queryGroupTotal(Map<String, Object> map) {
        return orderDao.queryGroupTotal(map);
    }

}
