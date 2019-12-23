package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.dao.OrderMapper;
import com.chundengtai.base.service.OrderService;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Royal
 * @since 2019-12-22
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public List<Order> queryList(HashMap<String, Object> hashMap) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status", hashMap.get("order_status"));
        queryWrapper.in("pay_status", 0, 1);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void autoCancelOrder() {
        baseMapper.autoCancelOrder();
    }

    public List<Order> completeOrder() {
        Map params = new HashMap();
        params.put("orderState", new int[]{OrderStatusEnum.CONFIRM_GOODS.getCode()});
        DateTime dateTime = new DateTime();
        DateTime beginTime = dateTime.plusDays(-7);
        params.put("beginTime", beginTime.toString("yyyy-MM-dd 00:00:00"));
        List<Order> result = this.listByMap(params);
        return result;
    }

    public void completeOrderStatus() {
        log.info("completeOrder开始执行");
        try {
            List<Order> orders = this.completeOrder();
//            for (OrderVo orderVo : orders) {
//                DividedOrderDto dividedOrderDto = new DividedOrderDto();
//                dividedOrderDto.setOrderId(Long.valueOf(orderVo.getId()));
//                dividedOrderDto.setOrderSn(orderVo.getOrder_sn());
//                orderVo.setOrder_status(ChujianOrderTypeEnum.COMPLETED_ORDER.getCode());
//                orderVo.setOrder_status_text(ChujianOrderTypeEnum.COMPLETED_ORDER.getDesc());
//                orderVo.setConfirm_time(new Date());
//                orderService.update(orderVo);
//                dividedService.completeOrder(dividedOrderDto);
//            }
        } catch (Exception ex) {
            log.error("error ", ex);
        }
        log.info("completeOrder完成 ");
    }
}
