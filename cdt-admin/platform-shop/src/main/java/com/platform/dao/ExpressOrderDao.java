package com.platform.dao;

import com.platform.entity.ExpressOrderEntity;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-12-05 16:43:50
 */
public interface ExpressOrderDao extends BaseDao<ExpressOrderEntity> {

    ExpressOrderEntity queryOrderId(Integer orderId);

}
