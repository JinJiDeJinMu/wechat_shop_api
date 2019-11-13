package com.chundengtai.base.server.dao;


import com.chundengtai.base.server.common.BaseBizMapper;
import com.chundengtai.base.server.entity.CommentVo;

import java.util.Map;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:14:26
 */
public interface ApiCommentMapper extends BaseBizMapper<CommentVo> {
    int queryhasPicTotal(Map<String, Object> map);
  
}
