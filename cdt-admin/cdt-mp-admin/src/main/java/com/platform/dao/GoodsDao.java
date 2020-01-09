package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.GoodsEntity;

/**
 * Dao
 *
 * @date 2017-08-21 21:19:49
 */
public interface GoodsDao extends BaseBizMapper<GoodsEntity> {
    Integer queryMaxId();
}
