package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.GoodsEntity;

public interface GoodsDao extends BaseBizMapper<GoodsEntity> {
    Integer queryMaxId();
}
