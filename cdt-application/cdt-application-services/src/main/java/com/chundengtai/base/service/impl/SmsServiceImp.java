package com.chundengtai.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonResponse;
import com.chundengtai.base.bean.CdtSmsLog;
import com.chundengtai.base.service.CdtSmsLogService;
import com.chundengtai.base.service.SmsService;
import com.chundengtai.base.utils.AliYunSmsUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class SmsServiceImp implements SmsService {

    @Autowired
    private CdtSmsLogService cdtSmsLogService;

    @Override
    public void sendSms(String PhoneNumbers, String TemplateCode, String TemplateParam) {

        CommonResponse commonResponse = AliYunSmsUtils.sendMessage(PhoneNumbers, TemplateCode, TemplateParam);
        if(commonResponse != null){
            String data = commonResponse.getData();
            JSONObject result = (JSONObject) JSON.toJSON(data);
            String[] phones = PhoneNumbers.split(",");
            for (int i = 0; i < phones.length; i++) {
                CdtSmsLog cdtSmsLog = new CdtSmsLog();
                cdtSmsLog.setPhone(phones[i]);
                cdtSmsLog.setSmsCode(result.get("Code").toString());
                cdtSmsLog.setSmsMessage(result.get("Message").toString());
                cdtSmsLog.setSmsSn(result.get("BizId").toString());
                cdtSmsLog.setTemplateId(TemplateCode);
                cdtSmsLog.setContent(TemplateParam);
                cdtSmsLog.setCreateTime(new Date());
                cdtSmsLogService.save(cdtSmsLog);
            }
        }
    }
}
