package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.CdtNewsBulletinMapper;
import com.chundengtai.base.entity.CdtNewsBulletinEntity;
import com.chundengtai.base.service.admin.CdtNewsBulletinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CdtNewsBulletinServiceImpl implements CdtNewsBulletinService {
    @Autowired
    private CdtNewsBulletinMapper cdtNewsBulletinMapper;

    @Override
    public CdtNewsBulletinEntity queryObject(Integer id) {
        return cdtNewsBulletinMapper.queryObject(id);
    }

    @Override
    public List<CdtNewsBulletinEntity> queryList(Map<String, Object> map) {
        return cdtNewsBulletinMapper.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return cdtNewsBulletinMapper.queryTotal(map);
    }

    @Override
    public int save(CdtNewsBulletinEntity cdtNewsBulletin) {
        return cdtNewsBulletinMapper.save(cdtNewsBulletin);
    }

    @Override
    public int update(CdtNewsBulletinEntity cdtNewsBulletin) {
        return cdtNewsBulletinMapper.update(cdtNewsBulletin);
    }

    @Override
    public int delete(Integer id) {
        return cdtNewsBulletinMapper.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return cdtNewsBulletinMapper.deleteBatch(ids);
    }
}
