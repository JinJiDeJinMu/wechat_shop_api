package com.chundengtai.base.service;

import com.chundengtai.base.bean.CdtDistrimoney;
import com.chundengtai.base.bean.Order;
import com.chundengtai.base.event.DistributionEvent;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :未名严选
 * 类 名 称    :
 * 功能描述    :
 * 业务描述    :
 * 作 者 名    :@Author amtf
 * 开发日期    :2020/1/2 下午2:29
 * Created    :IntelliJ IDEA
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
public interface IdistributionService {

    CdtDistrimoney getDistrimoney();

    void distributionLogic(DistributionEvent event);

    void referreRelation(long userId, String referrerEncpyt);

    void recordDistributeLog(Integer userId, Order order);

    boolean notifyOrderStatus(Integer userId, Order order, GoodsTypeEnum goodsTypeEnum);
}
