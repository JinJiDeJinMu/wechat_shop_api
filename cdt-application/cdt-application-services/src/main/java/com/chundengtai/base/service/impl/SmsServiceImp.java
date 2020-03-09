package com.chundengtai.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonResponse;
import com.chundengtai.base.bean.CdtSmsLog;
import com.chundengtai.base.service.CdtSmsLogService;
import com.chundengtai.base.service.SmsService;
import com.chundengtai.base.utils.AliYunSmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
@Slf4j
public class SmsServiceImp implements SmsService {

    @Autowired
    private CdtSmsLogService cdtSmsLogService;

    @Override
    public void sendSms(String PhoneNumbers, HashMap<String, Object> hashMap) {
        CommonResponse commonResponse = AliYunSmsUtils.send(PhoneNumbers, JSON.toJSONString(hashMap));
        log.info("smsService respose:"+commonResponse);
        if(commonResponse != null){
            JSONObject result = JSONObject.parseObject(commonResponse.getData());
            String[] phones = PhoneNumbers.split(",");
            for (int i = 0; i < phones.length; i++) {
                CdtSmsLog cdtSmsLog = new CdtSmsLog();
                cdtSmsLog.setPhone(phones[i]);
                cdtSmsLog.setSmsCode(result.get("Code").toString());
                cdtSmsLog.setSmsMessage(result.get("Message").toString());
                cdtSmsLog.setSmsSn(result.get("BizId").toString());
                cdtSmsLog.setTemplateId("");
                cdtSmsLog.setContent("");
                cdtSmsLog.setCreateTime(new Date());
                cdtSmsLogService.save(cdtSmsLog);
            }
        }
    }
}
