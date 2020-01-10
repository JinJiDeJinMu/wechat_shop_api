package com.chundengtai.base.service;


import com.chundengtai.base.dao.ApiCdtNewsBulletinMapper;
import com.chundengtai.base.entity.CdtNewsBulletinEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 消息公告Service接口
 *
 * @date 2019-11-25 11:38:00
 */
@Service
public class ApiCdtNewsBulletinService {
    @Autowired
    private ApiCdtNewsBulletinMapper cdtNewsBulletinMapper;

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    public CdtNewsBulletinEntity queryObject(Integer id) {
        return cdtNewsBulletinMapper.queryObject(id);
    }

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    public List<CdtNewsBulletinEntity> queryList(Map<String, Object> map) {
        return cdtNewsBulletinMapper.queryList(map);
    }

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    public int queryTotal(Map<String, Object> map) {
        return cdtNewsBulletinMapper.queryTotal(map);
    }

    /**
     * 保存实体
     *
     * @param cdtNewsBulletin 实体
     * @return 保存条数
     */
    public int save(CdtNewsBulletinEntity cdtNewsBulletin) {
        return cdtNewsBulletinMapper.save(cdtNewsBulletin);
    }

    /**
     * 根据主键更新实体
     *
     * @param cdtNewsBulletin 实体
     * @return 更新条数
     */
    public int update(CdtNewsBulletinEntity cdtNewsBulletin) {
        return cdtNewsBulletinMapper.update(cdtNewsBulletin);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    public int delete(Integer id) {
        return cdtNewsBulletinMapper.delete(id);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    public int deleteBatch(Integer[] ids) {
        return cdtNewsBulletinMapper.deleteBatch(ids);
    }
}
