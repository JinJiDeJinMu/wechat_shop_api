package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.SpecificationEntity;

import java.util.List;

/**
 * 规格表
 *
 * @date 2017-08-13 10:41:10
 */
public interface SpecificationDao extends BaseBizMapper<SpecificationEntity> {
    List<SpecificationEntity> queryListByGoodsId(String goodsId);

}
