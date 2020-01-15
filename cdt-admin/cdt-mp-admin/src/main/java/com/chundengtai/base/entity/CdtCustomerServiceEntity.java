package com.chundengtai.base.entity;

import java.io.Serializable;

public class CdtCustomerServiceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * 二维码地址
     */
    private String url;
    /**
     * 鎵嬫満鍙风爜
     */
    private String phone;

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
     * 设置：二维码地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取：二维码地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置：鎵嬫満鍙风爜
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取：鎵嬫満鍙风爜
     */
    public String getPhone() {
        return phone;
    }
}
