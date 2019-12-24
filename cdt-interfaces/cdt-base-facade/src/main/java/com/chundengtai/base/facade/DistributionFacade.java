package com.chundengtai.base.facade;

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
    public void recordDistributeLog(Integer userId, Integer orderId) {
        distributionService.recordDistributeLog(userId, orderId);
    }

    @Async
    public void notifyOrderStatus(Integer userId, Integer orderId, String orderNo, GoodsTypeEnum goodsTypeEnum) {
        distributionService.recordDistributeLog(userId, orderId);
    }
}
