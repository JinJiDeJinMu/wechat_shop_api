package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.CategoryEntity;

/**
 * Dao
 *
 * @date 2017-08-21 15:32:31
 */
public interface CategoryDao extends BaseBizMapper<CategoryEntity> {

    public int deleteByParentBatch(Object[] id);
}
