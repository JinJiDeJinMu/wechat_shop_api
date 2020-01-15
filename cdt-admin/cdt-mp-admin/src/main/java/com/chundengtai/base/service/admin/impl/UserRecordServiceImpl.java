package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.UserRecordDao;
import com.chundengtai.base.entity.UserRecordEntity;
import com.chundengtai.base.service.admin.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserRecordServiceImpl implements UserRecordService {

    @Autowired
    private UserRecordDao userRecordDao;


    @Override
    public List<UserRecordEntity> queryList(Map<String, Object> map) {
        return userRecordDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userRecordDao.queryTotal(map);
    }
}
