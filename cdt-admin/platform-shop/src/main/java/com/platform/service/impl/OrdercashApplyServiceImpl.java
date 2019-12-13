package com.platform.service.impl;


import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.platform.dao.OrdercashApplyDao;
import com.platform.entity.OrdercashApplyEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserRecord;
import com.platform.entity.UserVo;
import com.platform.service.OrdercashApplyService;
import com.platform.util.RedisUtils;
import com.platform.util.wechat.WechatRefundApiResult;
import com.platform.util.wechat.WechatUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-12-11 11:29:38
 */
@Service("ordercashApplyService")
public class OrdercashApplyServiceImpl implements OrdercashApplyService {
    @Autowired
    private OrdercashApplyDao ordercashApplyDao;
    private static Log logger = LogFactory.getLog(OrdercashApplyServiceImpl.class);

    @Override
    public OrdercashApplyEntity queryObject(Integer id) {
        return ordercashApplyDao.queryObject(id);
    }

    @Override
    public List<OrdercashApplyEntity> queryList(Map<String, Object> map) {
        return ordercashApplyDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return ordercashApplyDao.queryTotal(map);
    }

    @Override
    public int save(OrdercashApplyEntity ordercashApply) {
        return ordercashApplyDao.save(ordercashApply);
    }

    @Override
    public int update(OrdercashApplyEntity ordercashApply) {
        return ordercashApplyDao.update(ordercashApply);
    }

    @Override
    public int delete(Integer id) {
        return ordercashApplyDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return ordercashApplyDao.deleteBatch(ids);
    }

    @Override
    public int review(Integer id) { return ordercashApplyDao.review(id); }

    @Override
    public OrdercashApplyEntity query(Integer orderId) {
        return ordercashApplyDao.query(orderId);
    }

    /**
     * 提现
     * @param userEntity
     * @param amount
     * @return
     */
    @Override
    public boolean wechatMoneyToUser(UserEntity userEntity, Double amount) {

        String openId = userEntity.getWeixinOpenid();
        String name = userEntity.getRealName();
        String txKey = RedisUtils.get("backtx"+userEntity.getMerchantId());
        if(StringUtils.isNotBlank(txKey)) {
            return false;
        }
        //设置redisKsy
        RedisUtils.set("backtx"+userEntity.getMerchantId(), "10");
        String payCountId = UUID.randomUUID().toString().replaceAll("-", "");
        //开始调用提现微信接口
        WechatRefundApiResult ret = WechatUtil.wxPayMoneyToUser(openId, amount, name, payCountId);
        System.out.println(ret.getResult_code()+"++==="+ret.getReturn_msg());
        logger.info(ret.getResult_code()+"==="+ret.getReturn_msg());
        if("SUCCESS".equals(ret.getErr_code())) {
            RedisUtils.del("backtx"+userEntity.getMerchantId());
            return true;
        }
        return false;
    }


}
