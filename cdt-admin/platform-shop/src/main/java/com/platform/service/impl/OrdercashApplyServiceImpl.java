package com.platform.service.impl;


import com.platform.dao.OrdercashApplyDao;
import com.platform.entity.OrdercashApplyEntity;
import com.platform.service.OrdercashApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-12-11 11:29:38
 */
@Service("ordercashApplyService")
public class OrdercashApplyServiceImpl implements OrdercashApplyService {
    @Autowired
    private OrdercashApplyDao ordercashApplyDao;

    @Override
    public OrdercashApplyEntity queryObject(Integer id) {
        return ordercashApplyDao.queryObject(id);
    }

    @Override
    public List<OrdercashApplyEntity> queryList(Map<String, Object> map) {
        return ordercashApplyDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return ordercashApplyDao.queryTotal(map);
    }

    @Override
    public int save(OrdercashApplyEntity ordercashApply) {
        return ordercashApplyDao.save(ordercashApply);
    }

    @Override
    public int update(OrdercashApplyEntity ordercashApply) {
        return ordercashApplyDao.update(ordercashApply);
    }

    @Override
    public int delete(Integer id) {
        return ordercashApplyDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return ordercashApplyDao.deleteBatch(ids);
    }

    @Override
    public int review(Integer id) { return ordercashApplyDao.review(id); }

    @Override
    public OrdercashApplyEntity query(Integer orderId) {
        return ordercashApplyDao.query(orderId);
    }
}
