package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.FootprintEntity;

import java.util.List;
import java.util.Map;

public interface FootprintService {

    FootprintEntity queryObject(Integer id);

    List<FootprintEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(FootprintEntity footprint);

    void update(FootprintEntity footprint);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);
}
