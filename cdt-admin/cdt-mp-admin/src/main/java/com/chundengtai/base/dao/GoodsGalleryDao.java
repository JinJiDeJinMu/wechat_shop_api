package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.GoodsGalleryEntity;

import java.util.Map;

public interface GoodsGalleryDao extends BaseBizMapper<GoodsGalleryEntity> {
    int deleteByGoodsId(Map<String, Integer> map);
}
