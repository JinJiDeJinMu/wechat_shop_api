package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.CommentV2Dao;
import com.chundengtai.base.entity.CommentV2Entity;
import com.chundengtai.base.service.admin.CommentV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 评价表Service实现类
 *
 * @date 2019-11-26 11:27:34
 */
@Service("nideshopCommentV2Service")
public class CommentV2ServiceImpl implements CommentV2Service {
    @Autowired
    private CommentV2Dao CommentV2Dao;

    @Override
    public CommentV2Entity queryObject(Integer id) {
        return CommentV2Dao.queryObject(id);
    }

    @Override
    public List<CommentV2Entity> queryList(Map<String, Object> map) {
        return CommentV2Dao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return CommentV2Dao.queryTotal(map);
    }

    @Override
    public int save(CommentV2Entity nideshopCommentV2) {
        return CommentV2Dao.save(nideshopCommentV2);
    }

    @Override
    public int update(CommentV2Entity nideshopCommentV2) {
        return CommentV2Dao.update(nideshopCommentV2);
    }

    @Override
    public int delete(Integer id) {
        return CommentV2Dao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return CommentV2Dao.deleteBatch(ids);
    }

    @Override
    public int toggleStatus(CommentV2Entity comment) {
        CommentV2Entity commentEntity = queryObject(comment.getId());
        if (commentEntity.getStatus() == 1) {
            commentEntity.setStatus(0);
        } else if (commentEntity.getStatus() == 0) {
            commentEntity.setStatus(1);
        }
        return CommentV2Dao.update(commentEntity);
    }
}
