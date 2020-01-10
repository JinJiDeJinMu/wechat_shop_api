package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.GroupBuyingEntity;
import com.chundengtai.base.entity.OrderEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2017-08-13 10:41:09
 */
public interface OrderDao extends BaseBizMapper<OrderEntity> {
    List<GroupBuyingEntity> queryGroupList(Map<String, Object> map);

    int queryGroupTotal(Map<String, Object> map);

    HashMap<String, Object> getTotalSum(Map<String, Object> map);

}
