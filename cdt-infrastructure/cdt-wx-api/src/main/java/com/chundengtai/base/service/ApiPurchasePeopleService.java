package com.chundengtai.base.service;

import com.chundengtai.base.dao.ApiPurchasePeopleDao;
import com.chundengtai.base.entity.PurchasePeopleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Service接口
 *
 * @date 2019-11-25 09:39:01
 */
@Service
public class ApiPurchasePeopleService {
    @Autowired
    private ApiPurchasePeopleDao apiPurchasePeopleDao;

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    public PurchasePeopleEntity queryObject(Integer id) {
        return apiPurchasePeopleDao.queryObject(id);
    }

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    public List<PurchasePeopleEntity> queryList(Map<String, Object> map) {
        return apiPurchasePeopleDao.queryList(map);
    }

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    public int queryTotal(Map<String, Object> map) {
        return apiPurchasePeopleDao.queryTotal(map);
    }

    /**
     * 保存实体
     *
     * @param PurchasePeople 实体
     * @return 保存条数
     */
    public int save(PurchasePeopleEntity PurchasePeople) {
        return apiPurchasePeopleDao.save(PurchasePeople);
    }

    /**
     * 根据主键更新实体
     *
     * @param PurchasePeople 实体
     * @return 更新条数
     */
    public int update(PurchasePeopleEntity PurchasePeople) {
        return apiPurchasePeopleDao.update(PurchasePeople);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    public int delete(Integer id) {
        return apiPurchasePeopleDao.delete(id);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    public int deleteBatch(Integer[] ids) {
        return apiPurchasePeopleDao.deleteBatch(ids);
    }
}
