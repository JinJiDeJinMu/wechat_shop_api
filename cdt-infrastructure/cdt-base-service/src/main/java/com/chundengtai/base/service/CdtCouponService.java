package com.chundengtai.base.service;

import com.chundengtai.base.bean.CdtCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.dto.CdtCouponDao;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Royal
 * @since 2020-03-09
 */
public interface CdtCouponService extends IService<CdtCoupon> {

    void saveCoupon(CdtCouponDao couponDao);

}
