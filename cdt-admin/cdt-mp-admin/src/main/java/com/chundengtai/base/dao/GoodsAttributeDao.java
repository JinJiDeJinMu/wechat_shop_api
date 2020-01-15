package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.GoodsAttributeEntity;

public interface GoodsAttributeDao extends BaseBizMapper<GoodsAttributeEntity> {

    int updateByGoodsIdAttributeId(GoodsAttributeEntity goodsAttributeEntity);
}
