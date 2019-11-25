package com.platform.service.impl;
import com.platform.dao.CdtCustomerServiceDao;
import com.platform.entity.CdtCustomerServiceEntity;
import com.platform.service.CdtCustomerServiceService;
import com.platform.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 客服管理表Service实现类
 *
 * @author lipengjun
 * @date 2019-11-25 19:27:34
 */
@Service("cdtCustomerServiceService")
public class CdtCustomerServiceServiceImpl implements CdtCustomerServiceService {
    @Autowired
    private CdtCustomerServiceDao cdtCustomerServiceDao;

    @Override
    public CdtCustomerServiceEntity queryObject(Integer id) {
        return cdtCustomerServiceDao.queryObject(id);
    }

    @Override
    public List<CdtCustomerServiceEntity> queryList(Map<String, Object> map) {
        return cdtCustomerServiceDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return cdtCustomerServiceDao.queryTotal(map);
    }

    @Override
    public int save(CdtCustomerServiceEntity cdtCustomerService) {
        return cdtCustomerServiceDao.save(cdtCustomerService);
    }

    @Override
    public int update(CdtCustomerServiceEntity cdtCustomerService) {
        return cdtCustomerServiceDao.update(cdtCustomerService);
    }

    @Override
    public int delete(Integer id) {
        return cdtCustomerServiceDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return cdtCustomerServiceDao.deleteBatch(ids);
    }
}
