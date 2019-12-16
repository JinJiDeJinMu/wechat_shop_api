package com.platform.dao;

import com.platform.entity.CommentReq;

/**
 * 评价表Dao
 *
 * @author lipengjun
 * @date 2019-11-18 15:59:17
 */
public interface ApiCommentV2Mapper extends BaseDao<CommentReq> {

    int savecom(CommentReq commentReq);
}
