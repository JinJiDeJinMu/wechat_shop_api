package com.chundengtai.base.service.impl;

import com.chundengtai.base.service.ApplyCashService;
import com.platform.util.wechat.WechatRefundApiResult;
import com.platform.util.wechat.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author hujinbiao
 * @Date 2019年12月28日 0028 上午 11:37:22
 * @Version 1.0
 **/
@Slf4j
public class ApplyCashServiceImpl implements ApplyCashService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean wechatMoneyToUser(String weixinOpenid, String realName,Double amount) {

        if(StringUtils.isBlank(weixinOpenid) || StringUtils.isBlank(realName) || amount == null){
            log.info("请求参数不合法==" + "weixinOpenid=" + weixinOpenid + "realName=" + realName + "amount=" + amount);
            return false;
        }
       /* String txKey = (String) redisTemplate.opsForValue().get("backtx");
        if(StringUtils.isNotBlank(txKey)) {
            log.info("redis key " + "backtx" +  + " exists");
            return false;
        }*/
        //设置redisKsy
       // redisTemplate.opsForValue().set("backtx" + userEntity.getMerchantId(), "10", 180, TimeUnit.MINUTES);
        String payCountId = UUID.randomUUID().toString().replaceAll("-", "");
        //开始调用提现微信接口
        WechatRefundApiResult ret = WechatUtil.wxPayMoneyToUser(weixinOpenid, amount, realName, payCountId);
        log.info("WechatRefundApiResult =" + ret.getResult_code() + "==" + ret.getReturn_msg());
        if("SUCCESS".equals(ret.getResult_code())) {
           // redisTemplate.delete("backtx" + userEntity.getMerchantId());
            return true;
        }
        return false;
    }
}
