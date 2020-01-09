package com.platform.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.platform.entity.GoodsAttributeEntity;

/**
 * @date 2017-08-13 10:41:08
 */
public interface GoodsAttributeDao extends BaseBizMapper<GoodsAttributeEntity> {

    int updateByGoodsIdAttributeId(GoodsAttributeEntity goodsAttributeEntity);
}
