package com.chundengtai.base.service;


import java.util.HashMap;

/**
 * 短信发送
 */
public interface SmsService {
    void sendSms(String PhoneNumbers, HashMap<String, Object> hashMap);
}
