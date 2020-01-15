package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.CartEntity;

import java.util.List;
import java.util.Map;

public interface CartService {

    CartEntity queryObject(Integer id);

    List<CartEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(CartEntity cart);

    void update(CartEntity cart);

    void delete(Integer id);

    void deleteBatch(Integer[] ids);
}
