package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.UserCouponDao;
import com.chundengtai.base.entity.UserCouponEntity;
import com.chundengtai.base.service.admin.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userCouponService")
public class UserCouponServiceImpl implements UserCouponService {
    @Autowired
    private UserCouponDao userCouponDao;

    @Override
    public UserCouponEntity queryObject(Integer id) {
        return userCouponDao.queryObject(id);
    }

    @Override
    public List<UserCouponEntity> queryList(Map<String, Object> map) {
        return userCouponDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userCouponDao.queryTotal(map);
    }

    @Override
    public int save(UserCouponEntity userCoupon) {
        return userCouponDao.save(userCoupon);
    }

    @Override
    public int update(UserCouponEntity userCoupon) {
        return userCouponDao.update(userCoupon);
    }

    @Override
    public int delete(Integer id) {
        return userCouponDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return userCouponDao.deleteBatch(ids);
    }
}
