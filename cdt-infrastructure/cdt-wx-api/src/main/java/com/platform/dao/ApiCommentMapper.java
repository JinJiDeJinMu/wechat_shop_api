package com.platform.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.platform.entity.CommentVo;

import java.util.Map;

/**
 * @date 2017-08-11 09:14:26
 */
public interface ApiCommentMapper extends BaseBizMapper<CommentVo> {
    int queryhasPicTotal(Map<String, Object> map);

}
