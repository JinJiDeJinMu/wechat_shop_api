package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.CdtCustomerServiceDao;
import com.chundengtai.base.entity.CdtCustomerServiceEntity;
import com.chundengtai.base.service.admin.CdtCustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


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
