package com.chundengtai.base.facade;

import com.chundengtai.base.bean.Order;
import com.chundengtai.base.service.DistributionService;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DistributionFacade {

    @Autowired
    private DistributionService distributionService;

    @Async
    public void recordDistributeLog(Integer userId, Order order) {
        distributionService.recordDistributeLog(userId, order);
    }

    @Async
    public void notifyOrderStatus(Integer userId, Order order, GoodsTypeEnum goodsTypeEnum) {
        boolean result = distributionService.notifyOrderStatus(userId, order, goodsTypeEnum);

    }
}
