package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.ExpressOrderEntity;

/**
 * Dao
 *
 * @date 2019-12-05 16:43:50
 */
public interface ExpressOrderDao extends BaseBizMapper<ExpressOrderEntity> {

    ExpressOrderEntity queryOrderId(Integer orderId);

}
