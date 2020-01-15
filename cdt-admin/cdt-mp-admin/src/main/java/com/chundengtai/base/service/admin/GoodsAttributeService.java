package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.GoodsAttributeEntity;

import java.util.List;
import java.util.Map;

public interface GoodsAttributeService {

    GoodsAttributeEntity queryObject(Integer id);

    List<GoodsAttributeEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(GoodsAttributeEntity goodsAttribute);

    void update(GoodsAttributeEntity goodsAttribute);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);
}
