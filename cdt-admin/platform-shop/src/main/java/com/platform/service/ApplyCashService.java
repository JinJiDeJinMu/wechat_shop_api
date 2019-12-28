package com.platform.service;

/**
 * 提现service
 */
public interface ApplyCashService {

    Boolean wechatMoneyToUser(String weixinOpenid, String realName, Double amount);
}
