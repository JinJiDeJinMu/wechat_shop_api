package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.GoodsEntity;

/**
 * Dao
 *
 * @date 2017-08-21 21:19:49
 */
public interface GoodsDao extends BaseBizMapper<GoodsEntity> {
    Integer queryMaxId();
}
