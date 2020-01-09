package com.platform.service.impl;

import com.chundengtai.base.utils.R;
import com.platform.service.ApplyCashService;
import com.platform.util.wechat.WechatRefundApiResult;
import com.platform.util.wechat.WechatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Description TODO
 * @Author hujinbiao
 * @Date 2019年12月28日 0028 上午 11:37:22
 * @Version 1.0
 **/
@Service("cdtDistridetailApplyService")
@Slf4j
public class ApplyCashServiceImpl implements ApplyCashService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public R wechatMoneyToUser(String weixinOpenid, String realName, Double amount) {

        if (StringUtils.isBlank(weixinOpenid) || StringUtils.isBlank(realName) || amount == null) {
            log.info("请求参数不合法==" + "weixinOpenid=" + weixinOpenid + "realName=" + realName + "amount=" + amount);
            return R.error("参数不合法");
        }
        String payCountId = UUID.randomUUID().toString().replaceAll("-", "");
        //开始调用提现微信接口
        WechatRefundApiResult ret = WechatUtil.wxPayMoneyToUser(weixinOpenid, amount, realName, payCountId);
        System.out.println("====" + ret.getReturn_msg());
        log.info("WechatRefundApiResult =" + ret.getResult_code() + "==" + ret.getReturn_msg());
        if ("SUCCESS".equals(ret.getResult_code())) {
            return R.ok(ret.getReturn_msg());
        }
        return R.error(ret.getErr_code_des());
    }
}
