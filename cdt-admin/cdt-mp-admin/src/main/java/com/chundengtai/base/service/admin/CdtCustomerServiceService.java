package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.CdtCustomerServiceEntity;

import java.util.List;
import java.util.Map;


/**
 * 客服管理表Service接口
 *
 * @date 2019-11-25 19:27:34
 */
public interface CdtCustomerServiceService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    CdtCustomerServiceEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<CdtCustomerServiceEntity> queryList(Map<String, Object> map);

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
     * @param cdtCustomerService 实体
     * @return 保存条数
     */
    int save(CdtCustomerServiceEntity cdtCustomerService);

    /**
     * 根据主键更新实体
     *
     * @param cdtCustomerService 实体
     * @return 更新条数
     */
    int update(CdtCustomerServiceEntity cdtCustomerService);

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