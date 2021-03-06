package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.GoodsEntity;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

public interface GoodsService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    GoodsEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<GoodsEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param goods 实体
     * @return 保存条数
     */
    int save(GoodsEntity goods);

    /**
     * 根据主键更新实体
     *
     * @param goods 实体
     * @return 更新条数
     */
    int update(GoodsEntity goods);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Integer[] ids);

    /**
     * 商品从回收站恢复
     *
     * @param ids
     * @return
     */
    int back(Integer[] ids);

    /**
     * 上架
     *
     * @param id
     * @return
     */
    void enSale(Integer id);

    /**
     * 下架
     *
     * @param id
     * @return
     */
    void unSale(Integer id);

    /**
     * 热门
     *
     * @param id
     * @return
     */
    void enHot(Integer id);

    /**
     * 取消热门
     *
     * @param id
     * @return
     */
    void unHot(Integer id);

    /**
     * 新品
     * @param id
     */
    void toNew(Integer id);

    /**
     * 取消新品
     * @param id
     */
    void cancelNew(Integer id);
}
