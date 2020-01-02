package com.chundengtai.base.facade;

import com.chundengtai.base.bean.Order;
import com.chundengtai.base.service.IdistributionService;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@Slf4j
public class DistributionFacade implements IdistributionFacade {

    @Autowired
    private IdistributionService idistributionService;

    @Async
    @Override
    public void referreRelation(long userId, String referrerEncpyt) {
        log.info(" ===============referreRelation 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());

        idistributionService.referreRelation(userId, referrerEncpyt);
    }

    @Override
    @Async
    public void recordDistributeLog(Integer userId, Order order) {
        log.info(" ==============recordDistributeLog 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());
        idistributionService.recordDistributeLog(userId, order);
    }

    @Override
    @Async
    public void notifyOrderStatus(Integer userId, Order order, GoodsTypeEnum goodsTypeEnum) {
        log.info("================notifyOrderStatus 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());
        boolean result = idistributionService.notifyOrderStatus(userId, order, goodsTypeEnum);

    }
}
