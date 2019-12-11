package com.platform.dao;


import com.platform.entity.OrdercaseApplyEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-12-10 17:30:30
 */
public interface OrdercaseApplyDao extends BaseDao<OrdercaseApplyEntity> {

    int review(@Param("id") Integer id, @Param("status") Integer status);

}
