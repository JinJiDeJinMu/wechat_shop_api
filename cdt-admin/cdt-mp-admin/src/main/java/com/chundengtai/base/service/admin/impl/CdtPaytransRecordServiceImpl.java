package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.CdtAdminPaytransRecordDao;
import com.chundengtai.base.entity.CdtPaytransRecordEntity;
import com.chundengtai.base.service.KeygenService;
import com.chundengtai.base.service.admin.CdtPaytransRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 系统流水日志Service实现类
 *
 * @date 2019-12-06 10:27:31
 */
@Service("cdtPaytransRecordService")
public class CdtPaytransRecordServiceImpl implements CdtPaytransRecordService {
    @Autowired
    private KeygenService keygenService;

    @Autowired
    private CdtAdminPaytransRecordDao cdtPaytransRecordDao;

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
