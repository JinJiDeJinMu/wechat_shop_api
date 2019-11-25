package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.OrderGoodsVo;

import java.util.List;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:46
 */
public interface ApiOrderGoodsMapper extends BaseBizMapper<OrderGoodsVo> {
    List<OrderGoodsVo> queryInvalidOrder();
}
