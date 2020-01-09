package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.UserCouponVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @date 2017-08-11 09:16:47
 */
public interface ApiUserCouponMapper extends BaseBizMapper<UserCouponVo> {
    UserCouponVo queryByCouponNumber(@Param("coupon_number") String coupon_number);

    int queryUserGetTotal(Map userParams);

    void updateCouponStatus(UserCouponVo vo);
}
