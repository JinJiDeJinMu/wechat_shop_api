package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.OrdercashApplyEntity;
import org.apache.ibatis.annotations.Param;


/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-12-11 11:29:38
 */
public interface OrdercashApplyDao extends BaseBizMapper<OrdercashApplyEntity> {

    int review(@Param("id") Integer id);

    OrdercashApplyEntity query(@Param("id") Integer id);

}
