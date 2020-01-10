package com.chundengtai.base.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
@Component
public class WxMaProperties {

    private List<Config> configs;

    /**
     * 设置微信小程序的appid
     */
    @Value("${wx.appId}")
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    @Value("${wx.secret}")
    private String secret;

    /**
     * 设置微信小程序消息服务器配置的token
     */
    @Value("${wx.token}")
    private String token;

    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey
     */
    @Value("${wx.aesKey}")
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    @Value("${wx.msgDataFormat}")
    private String msgDataFormat;

    @Data
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        @Value("${wx.appId}")
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        @Value("${wx.secret}")
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        @Value("${wx.token}")
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        @Value("${wx.aesKey}")
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        @Value("${wx.msgDataFormat}")
        private String msgDataFormat;
    }

}
