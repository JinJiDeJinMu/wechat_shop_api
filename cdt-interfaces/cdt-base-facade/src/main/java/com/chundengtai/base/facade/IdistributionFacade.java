package com.chundengtai.base.facade;

import com.chundengtai.base.bean.Order;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import org.springframework.scheduling.annotation.Async;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :未名严选
 * 类 名 称    :
 * 功能描述    :
 * 业务描述    :
 * 作 者 名    :@Author Royal(方圆)
 * 开发日期    :2020/1/2 下午1:59
 * Created    :IntelliJ IDEA
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
public interface IdistributionFacade {
    @Async
    public void recordDistributeLog(Integer userId, Order order);

    @Async
    public void notifyOrderStatus(Integer userId, Order order, GoodsTypeEnum goodsTypeEnum);
}
