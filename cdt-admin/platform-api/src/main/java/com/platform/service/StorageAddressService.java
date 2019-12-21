package com.platform.service;

import com.platform.dao.StorageAddressDao;
import com.platform.entity.StorageAddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 存放地址表Service接口
 *
 * @date 2019-11-22 14:40:12
 */
@Service
public class StorageAddressService {

    @Autowired
    private StorageAddressDao storageAddressDao;

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    public StorageAddressEntity queryObject(Integer id){
        return storageAddressDao.queryObject(id);
    }

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    public List<StorageAddressEntity> queryList(Map<String, Object> map){
        return storageAddressDao.queryList(map);
    }

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    public int queryTotal(Map<String, Object> map){
        return storageAddressDao.queryTotal(map);
    }

    /**
     * 保存实体
     *
     * @param storageAddress 实体
     * @return 保存条数
     */
    public int save(StorageAddressEntity storageAddress){
      return   storageAddressDao.save(storageAddress);
    }

    /**
     * 根据主键更新实体
     *
     * @param storageAddress 实体
     * @return 更新条数
     */
    public int update(StorageAddressEntity storageAddress){
        return storageAddressDao.update(storageAddress);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    public int delete(Integer id){
        return storageAddressDao.delete(id);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    public int deleteBatch(Integer[] ids){
        return storageAddressDao.deleteBatch(ids);
    }
}
