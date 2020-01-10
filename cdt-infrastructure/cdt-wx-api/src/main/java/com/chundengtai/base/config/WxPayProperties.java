package com.chundengtai.base.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :
 * 类 名 称    :微信支付属性配置
 * 功能描述    :
 * 业务描述    :
 * 作 者 名    :@Author Royal(方圆)
 * 开发日期    :
 * Created     :IntelliJ IDEA
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
@Data
@Component
public class WxPayProperties {

    /**
     * 设置微信公众号或者小程序等的appid
     */
    @Value("${wx.appId}")
    private String appId;

    /**
     * 微信支付商户号
     */
    @Value("${wx.mchId}")
    private String mchId;

    /**
     * 微信支付商户密钥
     */
    @Value("${wx.paySignKey}")
    private String mchKey;

    @Value("${wx.paySignKey}")
    private String notifyUrl;

    /**
     * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除
     */
    private String subAppId;

    /**
     * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除
     */
    private String subMchId;

    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    @Value("${wx.keyPath}")
    private String keyPath;

}
