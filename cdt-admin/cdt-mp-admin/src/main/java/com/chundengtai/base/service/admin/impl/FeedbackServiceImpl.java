package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.FeedbackDao;
import com.chundengtai.base.entity.FeedbackEntity;
import com.chundengtai.base.service.admin.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @date 2017-08-23 15:03:25
 */
@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackDao feedbackDao;

    @Override
    public FeedbackEntity queryObject(Integer msgId) {
        return feedbackDao.queryObject(msgId);
    }

    @Override
    public List<FeedbackEntity> queryList(Map<String, Object> map) {
        return feedbackDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return feedbackDao.queryTotal(map);
    }

    @Override
    public int save(FeedbackEntity feedback) {
        return feedbackDao.save(feedback);
    }

    @Override
    public int update(FeedbackEntity feedback) {
        return feedbackDao.update(feedback);
    }

    @Override
    public int delete(Integer msgId) {
        return feedbackDao.delete(msgId);
    }

    @Override
    public int deleteBatch(Integer[] msgIds) {
        return feedbackDao.deleteBatch(msgIds);
    }
}
