package com.platform.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.platform.entity.GoodsGalleryEntity;

import java.util.Map;

/**
 * Dao
 *
 * @date 2017-08-23 14:41:43
 */
public interface GoodsGalleryDao extends BaseBizMapper<GoodsGalleryEntity> {
    int deleteByGoodsId(Map<String, Integer> map);
}
