package com.chundengtai.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chundengtai.base.bean.Order;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Royal
 * @since 2019-12-22
 */
public interface OrderMapper extends BaseMapper<Order> {

    void autoCancelOrder();
}
