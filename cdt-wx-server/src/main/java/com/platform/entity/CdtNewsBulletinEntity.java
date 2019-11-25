package com.platform.entity;

import java.io.Serializable;

/**
 * 消息公告实体
 * 表名 cdt_news_bulletin
 *
 * @author lipengjun
 * @date 2019-11-25 11:38:00
 */
public class CdtNewsBulletinEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * 内容
     */
    private String conent;
    /**
     * 跳转地址
     */
    private String url;

    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：内容
     */
    public void setConent(String conent) {
        this.conent = conent;
    }

    /**
     * 获取：内容
     */
    public String getConent() {
        return conent;
    }

    /**
     * 设置：跳转地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取：跳转地址
     */
    public String getUrl() {
        return url;
    }
}
