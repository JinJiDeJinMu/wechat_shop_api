package com.chundengtai.base.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.http.MethodType;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * 短信工具类
 */
public class AliYunSmsUtils {

   private static final String ACCESSKEYID = "LTAI4FrLCXaNQZ6Zfkng8LCJ";

   private static final String ACCESSSECRET = "7V0iGW8WDncrx2z3PYjdpQU6cttlYN";

   public static CommonResponse send(String PhoneNumbers,String TemplateParam){
       return sendMessage(PhoneNumbers,"未名严选","SMS_183150732",TemplateParam);
   }

    /**
     * 短信发送方法
     * @param PhoneNumbers 手机号码，多个使用，隔开
     * @param signName 签名
     * @param TemplateCode 模板ID
     * @param TemplateParam 模板参数{"name":"测试","code":"1111"}
     * @return
     */
    public static CommonResponse sendMessage(String PhoneNumbers, String signName, String TemplateCode, String TemplateParam){

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSSECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();

        request.putQueryParameter("PhoneNumbers",PhoneNumbers);
        request.putQueryParameter("SignName",signName);
        request.putQueryParameter("TemplateCode",TemplateCode);
        request.putQueryParameter("AccessKeyId",ACCESSKEYID);
        request.putQueryParameter("Action","SendSms");
        request.putQueryParameter("OutId","");
        request.putQueryParameter("SmsUpExtendCode","");
        request.putQueryParameter("TemplateParam",TemplateParam);
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        CommonResponse response = null;
        try {
            response = client.getCommonResponse(request);
        } catch (com.aliyuncs.exceptions.ServerException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name","zhs");
        hashMap.put("code","123456");
        String js = JSON.toJSONString(hashMap);
        CommonResponse response = AliYunSmsUtils.sendMessage("15669959631","未名严选","SMS_183150732","{\"name\":\"测试\",\"code\":\"1111\"}");
        JSONObject jsonObject = JSONObject.parseObject(response.getData());
        System.out.println(jsonObject);
        System.out.println(js);
    }
}
