package com.platform.service;


import com.platform.entity.OrdercashApplyEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author lipengjun
 * @date 2019-12-11 11:29:38
 */
public interface OrdercashApplyService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    OrdercashApplyEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<OrdercashApplyEntity> queryList(Map<String, Object> map);

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
     * @param ordercashApply 实体
     * @return 保存条数
     */
    int save(OrdercashApplyEntity ordercashApply);

    /**
     * 根据主键更新实体
     *
     * @param ordercashApply 实体
     * @return 更新条数
     */
    int update(OrdercashApplyEntity ordercashApply);

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
     * 修改状态
     * @param id
     * @return
     */
    int review(Integer id);

    /**
     * 查询申请订单是否存在
     * @param orderId
     * @return
     */
    OrdercashApplyEntity query(Integer orderId);
}
