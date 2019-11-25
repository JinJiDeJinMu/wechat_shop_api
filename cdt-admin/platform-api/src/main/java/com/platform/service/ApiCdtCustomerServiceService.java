package com.platform.service;

import com.platform.dao.ApiCdtCustomerServiceDao;
import com.platform.entity.CdtCustomerServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 客服管理表Service接口
 *
 * @author lipengjun
 * @date 2019-11-25 19:27:34
 */
@Service
public class ApiCdtCustomerServiceService {
    @Autowired
    private ApiCdtCustomerServiceDao apiCdtCustomerServiceDao;


    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    public CdtCustomerServiceVo queryObject(Integer id) {
        return apiCdtCustomerServiceDao.queryObject(id);
    }

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    public List<CdtCustomerServiceVo> queryList(Map<String, Object> map) {
        return apiCdtCustomerServiceDao.queryList(map);
    }

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    public int queryTotal(Map<String, Object> map) {
        return apiCdtCustomerServiceDao.queryTotal(map);
    }

    /**
     * 保存实体
     *
     * @param cdtCustomerServiceVo 实体
     * @return 保存条数
     */
    public int save(CdtCustomerServiceVo cdtCustomerServiceVo) {
        return apiCdtCustomerServiceDao.save(cdtCustomerServiceVo);
    }

    /**
     * 根据主键更新实体
     *
     * @param cdtCustomerService 实体
     * @return 更新条数
     */
    public int update(CdtCustomerServiceVo cdtCustomerService) {
        return apiCdtCustomerServiceDao.update(cdtCustomerService);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    public int delete(Integer id) {
        return apiCdtCustomerServiceDao.delete(id);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    public int deleteBatch(Integer[] ids) {
        return apiCdtCustomerServiceDao.deleteBatch(ids);
    }
}
