package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.OrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @date 2017-08-11 09:16:46
 */
public interface ApiOrderMapper extends BaseBizMapper<OrderVo> {
    public OrderVo queryOrderNo(String order_sn);

    public void updateStatus(OrderVo vo);

    public List<OrderVo> queryWaitList();

    public List<OrderVo> queryPageList(Map<String, Object> map);

    public List<OrderVo> queryFxList();

    public List<OrderVo> queryByAllOrderId(@Param("allOrderId") String allOrderId);

    public OrderVo queryByOrderId(@Param("allOrderId") String allOrderId);

    List<OrderVo> queryGroupBuyRefundList(Map map);
}
