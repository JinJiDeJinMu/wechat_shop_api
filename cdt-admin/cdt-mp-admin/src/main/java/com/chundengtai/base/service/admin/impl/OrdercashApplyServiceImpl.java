package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.OrdercashApplyDao;
import com.chundengtai.base.entity.OrdercashApplyEntity;
import com.chundengtai.base.entity.UserEntity;
import com.chundengtai.base.service.admin.OrdercashApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Service实现类
 *
 * @date 2019-12-11 11:29:38
 */
@Service("ordercashApplyService")
@Slf4j
public class OrdercashApplyServiceImpl implements OrdercashApplyService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private OrdercashApplyDao ordercashApplyDao;

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
    public int review(Integer id) {
        return ordercashApplyDao.review(id);
    }

    @Override
    public OrdercashApplyEntity query(Integer orderId) {
        return ordercashApplyDao.query(orderId);
    }

    /**
     * 提现
     *
     * @param userEntity
     * @param amount
     * @return
     */
    @Override
    public boolean wechatMoneyToUser(UserEntity userEntity, Double amount) {

        /*if(userEntity == null || userEntity.getMerchantId() == null){
            log.info("UserEntity is null or MerchantId is null or RealName is null");
            return false;
        }
        String txKey = (String) redisTemplate.opsForValue().get("backtx" + userEntity.getMerchantId());
        if(StringUtils.isNotBlank(txKey)) {
            log.info("redis key " + "backtx" + userEntity.getMerchantId() + " exists");
            return false;
        }
        //设置redisKsy
        redisTemplate.opsForValue().set("backtx" + userEntity.getMerchantId(), "10", 180, TimeUnit.MINUTES);
        String payCountId = UUID.randomUUID().toString().replaceAll("-", "");
        //开始调用提现微信接口
        WechatRefundApiResult ret = WechatUtil.wxPayMoneyToUser(userEntity.getWeixinOpenid(), amount, userEntity.getRealName(), payCountId);
        log.info("WechatRefundApiResult =" + ret.getResult_code() + "==" + ret.getReturn_msg());
        if("SUCCESS".equals(ret.getResult_code())) {
            redisTemplate.delete("backtx" + userEntity.getMerchantId());
            return true;
        }*/
        return false;
    }


}
