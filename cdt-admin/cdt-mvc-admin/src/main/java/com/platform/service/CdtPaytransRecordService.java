package com.platform.service;

import com.platform.entity.CdtPaytransRecordEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统流水日志Service接口
 *
 * @date 2019-12-06 10:27:31
 */
public interface CdtPaytransRecordService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    CdtPaytransRecordEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<CdtPaytransRecordEntity> queryList(Map<String, Object> map);

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
     * @param cdtPaytransRecord 实体
     * @return 保存条数
     */
    int save(CdtPaytransRecordEntity cdtPaytransRecord);

    /**
     * 根据主键更新实体
     *
     * @param cdtPaytransRecord 实体
     * @return 更新条数
     */
    int update(CdtPaytransRecordEntity cdtPaytransRecord);

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
}
