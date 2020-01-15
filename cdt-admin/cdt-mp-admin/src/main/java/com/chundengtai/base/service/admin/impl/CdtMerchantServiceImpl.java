package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.CdtAdminMerchantDao;
import com.chundengtai.base.entity.CdtMerchantEntity;
import com.chundengtai.base.service.KeygenService;
import com.chundengtai.base.service.admin.CdtMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("cdtMerchantService")
public class CdtMerchantServiceImpl implements CdtMerchantService {
    @Autowired
    private CdtAdminMerchantDao cdtMerchantDao;

    @Autowired
    private KeygenService keygenService;

    @Override
    public CdtMerchantEntity queryObject(Long id) {
        return cdtMerchantDao.queryObject(id);
    }

    @Override
    public List<CdtMerchantEntity> queryList(Map<String, Object> map) {
        return cdtMerchantDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return cdtMerchantDao.queryTotal(map);
    }

    @Override
    public int save(CdtMerchantEntity cdtMerchant) {
        //cdtMerchant.setId(keygenService.genNumber().longValue());
        return cdtMerchantDao.save(cdtMerchant);
    }

    @Override
    public int update(CdtMerchantEntity cdtMerchant) {
        return cdtMerchantDao.update(cdtMerchant);
    }

    @Override
    public int delete(Long id) {
        return cdtMerchantDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return cdtMerchantDao.deleteBatch(ids);
    }
}
