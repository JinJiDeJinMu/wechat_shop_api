package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.AttributeEntity;

import java.util.List;
import java.util.Map;

public interface AttributeService {

    AttributeEntity queryObject(Integer id);

    List<AttributeEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(AttributeEntity attribute);

    void update(AttributeEntity attribute);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);
}
