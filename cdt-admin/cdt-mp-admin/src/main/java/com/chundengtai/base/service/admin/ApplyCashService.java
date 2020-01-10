package com.chundengtai.base.service.admin;

import com.chundengtai.base.utils.R;

/**
 * 提现service
 */
public interface ApplyCashService {

    R wechatMoneyToUser(String weixinOpenid, String realName, Double amount);
}
