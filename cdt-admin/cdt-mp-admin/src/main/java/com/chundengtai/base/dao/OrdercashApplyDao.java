package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.OrdercashApplyEntity;
import org.apache.ibatis.annotations.Param;


/**
 * Dao
 */
public interface OrdercashApplyDao extends BaseBizMapper<OrdercashApplyEntity> {

    int review(@Param("id") Integer id);

    OrdercashApplyEntity query(@Param("id") Integer id);

}
