package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.CollectEntity;

import java.util.List;
import java.util.Map;


public interface CollectService {

    CollectEntity queryObject(Integer id);

    List<CollectEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(CollectEntity collect);

    void update(CollectEntity collect);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);
}
