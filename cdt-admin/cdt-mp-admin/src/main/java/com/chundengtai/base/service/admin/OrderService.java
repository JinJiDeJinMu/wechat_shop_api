package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.GroupBuyingEntity;
import com.chundengtai.base.entity.OrderEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderEntity queryObject(Integer id);

    List<OrderEntity> queryList(Map<String, Object> map);

    List<GroupBuyingEntity> queryGroupList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    int queryGroupTotal(Map<String, Object> map);

    int save(OrderEntity order);

    int update(OrderEntity order);

    int delete(Integer id);

    int deleteBatch(Integer[] ids);

    /**
     * 确定收货
     *
     * @param id
     * @return
     */
    int confirm(Integer id);

    int sendGoods(OrderEntity order);

    HashMap<String, Object> getTotalSum(Map<String, Object> map);
}
