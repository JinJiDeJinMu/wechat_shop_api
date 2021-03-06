
package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.ExpressOrderEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @date 2019-12-05 16:43:50
 */
public interface ExpressOrderService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    ExpressOrderEntity queryObject(Integer id);


    /**
     * 根据主键查询实体
     *
     * @param orderId 主键
     * @return 实体
     */
    ExpressOrderEntity queryOrderId(Integer orderId);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<ExpressOrderEntity> queryList(Map<String, Object> map);

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
     * @param nideshopExpressOrder 实体
     * @return 保存条数
     */
    int save(ExpressOrderEntity nideshopExpressOrder);

    /**
     * 根据主键更新实体
     *
     * @param nideshopExpressOrder 实体
     * @return 更新条数
     */
    int update(ExpressOrderEntity nideshopExpressOrder);

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
}
