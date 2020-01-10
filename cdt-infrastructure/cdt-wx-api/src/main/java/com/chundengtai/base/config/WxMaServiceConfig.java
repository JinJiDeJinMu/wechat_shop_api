package com.chundengtai.base.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxMaServiceConfig {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.token}")
    private String token;

    @Value("${wx.aesKey}")
    private String aesKey;

    @Value("${wx.msgDataFormat}")
    private String msgDataFormat;

    @Bean
    public WxMaService service() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(StringUtils.trimToNull(appId));
        config.setSecret(StringUtils.trimToNull(secret));
        config.setToken(StringUtils.trimToNull(token));
        config.setAesKey(StringUtils.trimToNull(aesKey));
        config.setMsgDataFormat(StringUtils.trimToNull(msgDataFormat));
        WxMaServiceImpl service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

}
