package com.chundengtai.base.service.impl;

import com.chundengtai.base.dao.CdtMerchantDao;
import com.chundengtai.base.entity.CdtMerchantEntity;
import com.chundengtai.base.service.CdtMerchantWxService;
import com.chundengtai.base.service.KeygenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商家Service实现类
 *
 * @date 2019-11-15 17:08:05
 */
@Service("cdtMerchantServiceFront")
public class CdtMerchantServiceImpl implements CdtMerchantWxService {
    @Autowired
    private CdtMerchantDao cdtMerchantDao;
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
        cdtMerchant.setId(keygenService.genNumber().longValue());
        return cdtMerchantDao.save(cdtMerchant);
    }

    @Override
    public int add(CdtMerchantEntity cdtMerchant) {
        return cdtMerchantDao.add(cdtMerchant);
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

    @Override
    public CdtMerchantEntity queryByGoodsId(Integer goodsid) {
        return cdtMerchantDao.queryByGoodsId(goodsid);
    }
}
