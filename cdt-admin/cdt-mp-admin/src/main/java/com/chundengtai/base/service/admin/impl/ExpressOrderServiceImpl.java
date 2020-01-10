package com.chundengtai.base.service.admin.impl;


import com.chundengtai.base.dao.ExpressOrderDao;
import com.chundengtai.base.entity.ExpressOrderEntity;
import com.chundengtai.base.service.admin.ExpressOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @date 2019-12-05 16:43:50
 */
@Service(" ExpressOrderService")
public class ExpressOrderServiceImpl implements ExpressOrderService {
    @Autowired
    private ExpressOrderDao ExpressOrderDao;

    @Override
    public ExpressOrderEntity queryObject(Integer id) {
        return ExpressOrderDao.queryObject(id);
    }

    @Override
    public ExpressOrderEntity queryOrderId(Integer orderId) {
        return ExpressOrderDao.queryOrderId(orderId);
    }

    @Override
    public List<ExpressOrderEntity> queryList(Map<String, Object> map) {
        return ExpressOrderDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return ExpressOrderDao.queryTotal(map);
    }

    @Override
    public int save(ExpressOrderEntity ExpressOrder) {
        return ExpressOrderDao.save(ExpressOrder);
    }

    @Override
    public int update(ExpressOrderEntity ExpressOrder) {
        return ExpressOrderDao.update(ExpressOrder);
    }

    @Override
    public int delete(Integer id) {
        return ExpressOrderDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return ExpressOrderDao.deleteBatch(ids);
    }
}
