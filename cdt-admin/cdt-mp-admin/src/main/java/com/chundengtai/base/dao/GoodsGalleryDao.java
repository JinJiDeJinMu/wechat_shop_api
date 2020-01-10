package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.GoodsGalleryEntity;

import java.util.Map;

/**
 * Dao
 *
 * @date 2017-08-23 14:41:43
 */
public interface GoodsGalleryDao extends BaseBizMapper<GoodsGalleryEntity> {
    int deleteByGoodsId(Map<String, Integer> map);
}
