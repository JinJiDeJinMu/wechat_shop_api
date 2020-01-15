package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.RelatedGoodsEntity;

import java.util.List;
import java.util.Map;

public interface RelatedGoodsService {

    RelatedGoodsEntity queryObject(Integer id);

    List<RelatedGoodsEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(RelatedGoodsEntity relatedGoods);

    void update(RelatedGoodsEntity relatedGoods);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);
}
