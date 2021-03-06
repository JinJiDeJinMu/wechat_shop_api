package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.CartVo;
import com.chundengtai.base.entity.MerCartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @date 2017-08-11 09:14:25
 */
public interface ApiCartMapper extends BaseBizMapper<CartVo> {
    void updateCheck(@Param("productIds") String[] productIds,
                     @Param("isChecked") Integer isChecked, @Param("userId") Long userId);

    void deleteByProductIds(@Param("productIds") String[] productIds);

    void deleteByUserAndProductIds(@Param("user_id") Long user_id, @Param("productIds") String[] productIds);

    void deleteByCart(@Param("user_id") Long user_id, @Param("session_id") Integer session_id, @Param("checked") Integer checked);

    List<CartVo> queryMrchantGroup(Map<String, Object> map);

    List<MerCartVo> queryMerCartList(Long userId);

    List<CartVo> queryCheckedByUserIdAndMerId(Map map);

    String queryMerchantName(Long merchantId);

    List<CartVo> queryCarts(Long userId);

}
