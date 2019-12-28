package com.chundengtai.base.service;

import org.springframework.stereotype.Service;

/**
 * 提现service
 */
@Service
public interface ApplyCashService {

    Boolean wechatMoneyToUser(String weixinOpenid, String realName,Double amount);
}
