package com.chundengtai.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.chundengtai.base.bean.CdtCoupon;
import com.chundengtai.base.bean.CdtCouponGoods;
import com.chundengtai.base.bean.dto.CdtCouponDao;
import com.chundengtai.base.dao.CdtCouponMapper;
import com.chundengtai.base.service.CdtCouponGoodsService;
import com.chundengtai.base.service.CdtCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Royal
 * @since 2020-03-09
 */
@Service
public class CdtCouponServiceImpl extends ServiceImpl<CdtCouponMapper, CdtCoupon> implements CdtCouponService {


    @Autowired
    private CdtCouponGoodsService cdtCouponGoodsService;

    @Override
    @Transactional
    public void saveCoupon(CdtCouponDao couponDao) {

        CdtCoupon coupon = BeanUtil.toBean(couponDao,CdtCoupon.class);
        this.save(coupon);
        System.out.println("==="+coupon);
        CdtCouponGoods cdtCouponGoods = new CdtCouponGoods();
        cdtCouponGoods.setCouponId(coupon.getId());
        if(couponDao.getUseType() == 1){
            cdtCouponGoods.setCategoryId(couponDao.getCategoryId());
        }else if(couponDao.getUseType() == 2){
            cdtCouponGoods.setGoodsId(couponDao.getGoodIds());
        }
        cdtCouponGoodsService.save(cdtCouponGoods);
    }
}
