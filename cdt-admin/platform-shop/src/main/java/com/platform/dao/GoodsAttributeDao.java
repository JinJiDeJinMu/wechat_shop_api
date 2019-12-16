package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.GoodsAttributeEntity;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-13 10:41:08
 */
public interface GoodsAttributeDao extends BaseBizMapper<GoodsAttributeEntity> {

    int updateByGoodsIdAttributeId(GoodsAttributeEntity goodsAttributeEntity);
}
