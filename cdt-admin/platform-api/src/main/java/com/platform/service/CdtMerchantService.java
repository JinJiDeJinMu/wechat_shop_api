package com.platform.service;

import com.platform.entity.CdtMerchantEntity;

import java.util.List;
import java.util.Map;

/**
 * 商家Service接口
 *
 * @author lipengjun
 * @date 2019-11-15 17:08:05
 */
public interface CdtMerchantService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    CdtMerchantEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<CdtMerchantEntity> queryList(Map<String, Object> map);

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
     * @param cdtMerchant 实体
     * @return 保存条数
     */
    int save(CdtMerchantEntity cdtMerchant);

    /**
     * 根据主键更新实体
     *
     * @param cdtMerchant 实体
     * @return 更新条数
     */
    int update(CdtMerchantEntity cdtMerchant);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Long id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Long[] ids);

    /**
     * 根据商品id获取商家信息
     * @param goodsid
     * @return
     */
    CdtMerchantEntity queryByGoodsId(Integer goodsid);
}
