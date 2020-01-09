package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.CustomerEntity;

import java.util.List;
import java.util.Map;

/**
 * 客户Dao
 */
public interface CustomerDao extends BaseBizMapper<CustomerEntity> {
    List<CustomerEntity> query2List(Map<String, Object> map);

    int query2Total(Map<String, Object> map);
}
