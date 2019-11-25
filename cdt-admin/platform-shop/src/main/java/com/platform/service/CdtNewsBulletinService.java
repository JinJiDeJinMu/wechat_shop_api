package com.platform.service;


import com.platform.dao.CdtNewsBulletinMapper;
import com.platform.entity.CdtNewsBulletinEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 消息公告Service接口
 *
 * @author lipengjun
 * @date 2019-11-25 11:38:00
 */
public interface CdtNewsBulletinService {


    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    CdtNewsBulletinEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<CdtNewsBulletinEntity> queryList(Map<String, Object> map);

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
     * @param cdtNewsBulletin 实体
     * @return 保存条数
     */
    int save(CdtNewsBulletinEntity cdtNewsBulletin);

    /**
     * 根据主键更新实体
     *
     * @param cdtNewsBulletin 实体
     * @return 更新条数
     */
    int update(CdtNewsBulletinEntity cdtNewsBulletin);

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
