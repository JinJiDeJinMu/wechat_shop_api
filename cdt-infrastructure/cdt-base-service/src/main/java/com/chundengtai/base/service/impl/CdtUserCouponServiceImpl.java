package com.chundengtai.base.service.impl;

import com.chundengtai.base.bean.CdtUserCoupon;
import com.chundengtai.base.bean.dto.CdtUserCouponDao;
import com.chundengtai.base.dao.CdtUserCouponMapper;
import com.chundengtai.base.service.CdtUserCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Royal
 * @since 2020-03-16
 */
@Service
public class CdtUserCouponServiceImpl extends ServiceImpl<CdtUserCouponMapper, CdtUserCoupon> implements CdtUserCouponService {

    @Autowired
    private CdtUserCouponMapper cdtUserCouponMapper;

    @Override
    public List<CdtUserCouponDao> getUserCounponList() {

        return cdtUserCouponMapper.getUserCounponList();
    }
}
