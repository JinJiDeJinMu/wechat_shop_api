package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.CommentVo;

import java.util.Map;

/**
 * @date 2017-08-11 09:14:26
 */
public interface ApiCommentMapper extends BaseBizMapper<CommentVo> {
    int queryhasPicTotal(Map<String, Object> map);

}
