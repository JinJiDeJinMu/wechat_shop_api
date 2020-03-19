package com.chundengtai.base.service;

import com.chundengtai.base.bean.CdtCoupon;
import com.chundengtai.base.bean.CdtUserCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.dto.CdtUserCouponDao;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Royal
 * @since 2020-03-16
 */
public interface CdtUserCouponService extends IService<CdtUserCoupon> {

    List<CdtUserCouponDao> getUserCounponList(Map<String,Object> params);

    void doUserCoupon(CdtUserCoupon cdtUserCoupon, CdtCoupon cdtCoupon);

}
