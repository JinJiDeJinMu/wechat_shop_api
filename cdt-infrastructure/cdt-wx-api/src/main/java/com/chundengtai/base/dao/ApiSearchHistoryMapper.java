package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.SearchHistoryVo;
import org.apache.ibatis.annotations.Param;

/**
 * @date 2017-08-11 09:16:46
 */
public interface ApiSearchHistoryMapper extends BaseBizMapper<SearchHistoryVo> {
    int deleteByUserId(@Param("user_id") Long userId);
}
