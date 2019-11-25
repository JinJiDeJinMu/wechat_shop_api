package com.platform.entity;

import java.io.Serializable;

/**
 * 存放地址表实体
 * 表名 storage_address
 *
 * @author lipengjun
 * @date 2019-11-22 14:40:12
 */
public class StorageAddressEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;
    /**
     * 使用者id
     */
    private Integer userId;

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
     * 设置：省
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取：省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置：市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取：市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置：区
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取：区
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置：使用者id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：使用者id
     */
    public Integer getUserId() {
        return userId;
    }
}
