package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.TopicCategoryDao;
import com.chundengtai.base.entity.TopicCategoryEntity;
import com.chundengtai.base.service.admin.TopicCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("topicCategoryService")
public class TopicCategoryServiceImpl implements TopicCategoryService {
    @Autowired
    private TopicCategoryDao topicCategoryDao;

    @Override
    public TopicCategoryEntity queryObject(Integer id) {
        return topicCategoryDao.queryObject(id);
    }

    @Override
    public List<TopicCategoryEntity> queryList(Map<String, Object> map) {
        return topicCategoryDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return topicCategoryDao.queryTotal(map);
    }

    @Override
    public int save(TopicCategoryEntity topicCategory) {
        return topicCategoryDao.save(topicCategory);
    }

    @Override
    public int update(TopicCategoryEntity topicCategory) {
        return topicCategoryDao.update(topicCategory);
    }

    @Override
    public int delete(Integer id) {
        return topicCategoryDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return topicCategoryDao.deleteBatch(ids);
    }
}
