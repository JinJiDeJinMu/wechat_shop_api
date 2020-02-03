package com.chundengtai.base.service;

/**
 * 短信发送
 */
public interface SmsService {

    /**
     * 发送短信（json参数）
     * @param PhoneNumbers 手机号码，多个使用，隔开
     * @param TemplateCode 模板ID
     * @param TemplateParam 模板参数{"code":"1111"}
     */
    void sendSms(String PhoneNumbers, String TemplateCode, String TemplateParam);
}
