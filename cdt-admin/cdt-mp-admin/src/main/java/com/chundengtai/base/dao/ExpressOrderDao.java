package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.ExpressOrderEntity;

public interface ExpressOrderDao extends BaseBizMapper<ExpressOrderEntity> {

    ExpressOrderEntity queryOrderId(Integer orderId);

}
