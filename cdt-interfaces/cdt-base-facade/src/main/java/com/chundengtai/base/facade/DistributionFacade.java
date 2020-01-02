package com.chundengtai.base.facade;

import com.chundengtai.base.bean.Order;
import com.chundengtai.base.service.DistributionService;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class DistributionFacade implements IdistributionFacade {

    @Autowired
    private DistributionService distributionService;

    @Override
    @Async
    public void recordDistributeLog(Integer userId, Order order) {
        distributionService.recordDistributeLog(userId, order);
    }

    @Override
    @Async
    public void notifyOrderStatus(Integer userId, Order order, GoodsTypeEnum goodsTypeEnum) {
        boolean result = distributionService.notifyOrderStatus(userId, order, goodsTypeEnum);

    }
}
