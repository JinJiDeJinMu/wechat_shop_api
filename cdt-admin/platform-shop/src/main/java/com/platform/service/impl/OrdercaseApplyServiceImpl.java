package com.platform.service.impl;


import com.platform.dao.OrdercaseApplyDao;
import com.platform.entity.OrdercaseApplyEntity;
import com.platform.service.OrdercaseApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-12-10 17:30:30
 */
@Service("OrdercaseApplyService")
public class OrdercaseApplyServiceImpl implements OrdercaseApplyService {
    @Autowired
    private OrdercaseApplyDao ordercaseApplyDao;

    @Override
    public OrdercaseApplyEntity queryObject(Integer id) {
        return ordercaseApplyDao.queryObject(id);
    }

    @Override
    public List<OrdercaseApplyEntity> queryList(Map<String, Object> map) {
        return ordercaseApplyDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return ordercaseApplyDao.queryTotal(map);
    }

    @Override
    public int save(OrdercaseApplyEntity OrdercaseApply) {
        return ordercaseApplyDao.save(OrdercaseApply);
    }

    @Override
    public int update(OrdercaseApplyEntity OrdercaseApply) {
        return ordercaseApplyDao.update(OrdercaseApply);
    }

    @Override
    public int delete(Integer id) {
        return ordercaseApplyDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return ordercaseApplyDao.deleteBatch(ids);
    }
}
