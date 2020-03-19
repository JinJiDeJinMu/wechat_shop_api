package com.chundengtai.base.service.impl;

import com.chundengtai.base.bean.CdtCoupon;
import com.chundengtai.base.bean.CdtUserCoupon;
import com.chundengtai.base.bean.dto.CdtUserCouponDao;
import com.chundengtai.base.dao.CdtUserCouponMapper;
import com.chundengtai.base.service.CdtCouponService;
import com.chundengtai.base.service.CdtUserCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private CdtCouponService cdtCouponService;

    @Override
    public List<CdtUserCouponDao> getUserCounponList(Map<String,Object> params) {

        return cdtUserCouponMapper.getUserCounponList(params);
    }

    @Override
    @Transactional
    public void doUserCoupon(CdtUserCoupon cdtUserCoupon, CdtCoupon cdtCoupon) {
        cdtUserCouponMapper.insert(cdtUserCoupon);
        cdtCoupon.setTotalCount(cdtCoupon.getTotalCount() - 1);
        cdtCoupon.setReceiveCount(cdtCoupon.getReceiveCount() + 1);
        cdtCouponService.updateById(cdtCoupon);
    }
}
