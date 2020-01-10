package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.KeywordsVo;

import java.util.List;
import java.util.Map;

/**
 * 热闹关键词表
 *
 * @date 2017-08-11 09:16:46
 */
public interface ApiKeywordsMapper extends BaseBizMapper<KeywordsVo> {
    List<Map> hotKeywordList(Map param);
}
