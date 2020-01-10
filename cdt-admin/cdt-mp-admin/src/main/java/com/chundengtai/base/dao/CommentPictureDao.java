package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.CommentPictureEntity;

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
