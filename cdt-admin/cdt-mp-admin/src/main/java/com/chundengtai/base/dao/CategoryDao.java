package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.CategoryEntity;

public interface CategoryDao extends BaseBizMapper<CategoryEntity> {

    public int deleteByParentBatch(Object[] id);
}
