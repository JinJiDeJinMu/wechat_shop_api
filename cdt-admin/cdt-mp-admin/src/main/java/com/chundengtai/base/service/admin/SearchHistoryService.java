package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.SearchHistoryEntity;

import java.util.List;
import java.util.Map;

public interface SearchHistoryService {

    SearchHistoryEntity queryObject(Integer id);

    List<SearchHistoryEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(SearchHistoryEntity searchHistory);

    void update(SearchHistoryEntity searchHistory);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);
}
