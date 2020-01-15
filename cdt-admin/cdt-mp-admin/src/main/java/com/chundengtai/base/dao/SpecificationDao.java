package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.SpecificationEntity;

import java.util.List;

public interface SpecificationDao extends BaseBizMapper<SpecificationEntity> {
    List<SpecificationEntity> queryListByGoodsId(String goodsId);

}
