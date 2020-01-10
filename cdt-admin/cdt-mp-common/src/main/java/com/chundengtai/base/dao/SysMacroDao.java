package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.SysMacroEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用字典表Dao
 */
public interface SysMacroDao extends BaseBizMapper<SysMacroEntity> {

    /**
     * 查询数据字段
     *
     * @param value
     * @return
     */
    List<SysMacroEntity> queryMacrosByValue(@Param("value") String value);
}
