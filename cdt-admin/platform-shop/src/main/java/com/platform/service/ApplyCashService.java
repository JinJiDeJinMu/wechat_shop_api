package com.platform.service;

import com.platform.utils.R;

/**
 * 提现service
 */
public interface ApplyCashService {

    R wechatMoneyToUser(String weixinOpenid, String realName, Double amount);
}
