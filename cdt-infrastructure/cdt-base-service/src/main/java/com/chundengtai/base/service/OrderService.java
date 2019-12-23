package com.chundengtai.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.Order;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Royal
 * @since 2019-12-22
 */
public interface OrderService extends IService<Order> {

    List<Order> queryList(HashMap<String, Object> hashMap);

    void autoCancelOrder();

}
