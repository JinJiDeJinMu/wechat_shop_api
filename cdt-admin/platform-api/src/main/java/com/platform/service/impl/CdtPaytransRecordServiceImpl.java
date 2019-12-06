package com.platform.service.impl;

import com.platform.dao.CdtPaytransRecordDao;
import com.platform.entity.CdtPaytransRecordEntity;
import com.platform.service.CdtPaytransRecordService;
import com.platform.service.KeygenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 系统流水日志Service实现类
 *
 * @author lipengjun
 * @date 2019-12-06 10:27:31
 */
@Service("cdtPaytransRecordService")
public class CdtPaytransRecordServiceImpl implements CdtPaytransRecordService {
    @Autowired
    private CdtPaytransRecordDao cdtPaytransRecordDao;

    @Autowired
    private KeygenService keygenService;

    @Override
    public CdtPaytransRecordEntity queryObject(Long id) {
        return cdtPaytransRecordDao.queryObject(id);
    }

    @Override
    public List<CdtPaytransRecordEntity> queryList(Map<String, Object> map) {
        return cdtPaytransRecordDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return cdtPaytransRecordDao.queryTotal(map);
    }

    @Override
    public int save(CdtPaytransRecordEntity cdtPaytransRecord) {
        cdtPaytransRecord.setId(keygenService.genNumber().longValue());
        return cdtPaytransRecordDao.save(cdtPaytransRecord);
    }

    @Override
    public int update(CdtPaytransRecordEntity cdtPaytransRecord) {
        return cdtPaytransRecordDao.update(cdtPaytransRecord);
    }

    @Override
    public int delete(Long id) {
        return cdtPaytransRecordDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return cdtPaytransRecordDao.deleteBatch(ids);
    }
}
