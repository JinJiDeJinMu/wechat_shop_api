package com.chundengtai.base.dao;

import com.chundengtai.base.bean.CdtUserCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chundengtai.base.bean.dto.CdtUserCouponDao;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Royal
 * @since 2020-03-16
 */
public interface CdtUserCouponMapper extends BaseMapper<CdtUserCoupon> {

    List<CdtUserCouponDao> getUserCounponList(Map<String,Object> params);

    void updateStatus(CdtUserCoupon cdtUserCoupon);

}
