package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.SearchHistoryDao;
import com.chundengtai.base.entity.SearchHistoryEntity;
import com.chundengtai.base.service.admin.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("searchHistoryService")
public class SearchHistoryServiceImpl implements SearchHistoryService {
    @Autowired
    private SearchHistoryDao searchHistoryDao;

    @Override
    public SearchHistoryEntity queryObject(Integer id) {
        return searchHistoryDao.queryObject(id);
    }

    @Override
    public List<SearchHistoryEntity> queryList(Map<String, Object> map) {
        return searchHistoryDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return searchHistoryDao.queryTotal(map);
    }

    @Override
    public void save(SearchHistoryEntity searchHistory) {
        searchHistoryDao.save(searchHistory);
    }

    @Override
    public void update(SearchHistoryEntity searchHistory) {
        searchHistoryDao.update(searchHistory);
    }

    @Override
    public void delete(Integer id) {
        searchHistoryDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        searchHistoryDao.deleteBatch(ids);
    }

}
