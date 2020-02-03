package com.chundengtai.base.utils;

import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.http.MethodType;
import com.chundengtai.base.utils.ResourceUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;

import java.rmi.ServerException;
import java.util.HashMap;

/**
 * 短信工具类
 */
public class AliYunSmsUtils {

    private static final String ACCESSKEYID = ResourceUtil.getConfigByName("accessKeyId");

    private static final String ACCESSSECRET = ResourceUtil.getConfigByName("accessSecret");

    /**
     * 短信发送方法
     * @param PhoneNumbers 手机号码，多个使用，隔开
     * @param TemplateCode 模板ID
     * @param TemplateParam 模板参数{"code":"1111"}
     * @return
     */
    public static CommonResponse sendMessage(String PhoneNumbers, String TemplateCode, String TemplateParam){

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEYID, ACCESSSECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();

        request.putQueryParameter("PhoneNumbers",PhoneNumbers);
        request.putQueryParameter("SignName","阿里云");
        request.putQueryParameter("TemplateCode",TemplateCode);
        request.putQueryParameter("AccessKeyId",ACCESSKEYID);
        request.putQueryParameter("Action","SendSms");
        request.putQueryParameter("OutId","");
        request.putQueryParameter("SmsUpExtendCode","");
        request.putQueryParameter("TemplateParam",TemplateParam);
        request.putQueryParameter("RegionId", "cn-hangzhou");
        CommonResponse response = null;
        try {
            response = client.getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ServerException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return response;
    }
}
