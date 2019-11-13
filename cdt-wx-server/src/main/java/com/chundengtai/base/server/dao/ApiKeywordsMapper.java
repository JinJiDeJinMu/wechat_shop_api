package com.chundengtai.base.server.dao;


import com.chundengtai.base.server.common.BaseBizMapper;
import com.chundengtai.base.server.entity.KeywordsVo;

import java.util.List;
import java.util.Map;

/**
 * 热闹关键词表
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:46
 */
public interface ApiKeywordsMapper extends BaseBizMapper<KeywordsVo> {
    List<Map> hotKeywordList(Map param);
}
