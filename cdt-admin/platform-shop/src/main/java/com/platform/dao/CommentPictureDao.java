package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.CommentPictureEntity;

/**
 * 评价图片Dao
 *
 * @date 2017-08-29 14:45:55
 */
public interface CommentPictureDao extends BaseBizMapper<CommentPictureEntity> {
    /**
     * 根据commentId删除
     *
     * @param commentId
     * @return
     */
    int deleteByCommentId(Integer commentId);

    int deleteByCommentIds(Object[] id);
}
