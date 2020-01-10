package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.TopicDao;
import com.chundengtai.base.entity.TopicEntity;
import com.chundengtai.base.service.admin.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @date 2017-08-20 14:10:08
 */
@Service("topicService")
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicDao topicDao;

    @Override
    public TopicEntity queryObject(Integer id) {
        return topicDao.queryObject(id);
    }

    @Override
    public List<TopicEntity> queryList(Map<String, Object> map) {
        return topicDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return topicDao.queryTotal(map);
    }

    @Override
    public int save(TopicEntity topic) {
        return topicDao.save(topic);
    }

    @Override
    public int update(TopicEntity topic) {
        return topicDao.update(topic);
    }

    @Override
    public int delete(Integer id) {
        return topicDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return topicDao.deleteBatch(ids);
    }
}
