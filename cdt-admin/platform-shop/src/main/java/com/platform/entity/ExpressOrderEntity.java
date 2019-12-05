package com.platform.entity;

import java.io.Serializable;

/**
 * 实体
 * 表名 nideshop_express_order
 *
 * @author lipengjun
 * @date 2019-12-05 16:43:50
 */
public class ExpressOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;
    /**
     * 
     */
    private Integer oederId;
    /**
     * 
     */
    private Integer userId;
    /**
     * 国家
     */
    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 街道
     */
    private String district;
    /**
     * 地址
     */
    private String address;
    /**
     * 收货手机
     */
    private String mobile;
    /**
     * 姓名
     */
    private String name;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 配送时间
     */
    private String pickNumber;
    /**
     * 
     */
    private Integer orderId;
    /**
     * 快递单号
     */
    private String expressNo;

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
     * 设置：
     */
    public void setOederId(Integer oederId) {
        this.oederId = oederId;
    }

    /**
     * 获取：
     */
    public Integer getOederId() {
        return oederId;
    }
    /**
     * 设置：
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：国家
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 获取：国家
     */
    public String getCountry() {
        return country;
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
     * 设置：街道
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * 获取：街道
     */
    public String getDistrict() {
        return district;
    }
    /**
     * 设置：地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取：地址
     */
    public String getAddress() {
        return address;
    }
    /**
     * 设置：收货手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取：收货手机
     */
    public String getMobile() {
        return mobile;
    }
    /**
     * 设置：姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：姓名
     */
    public String getName() {
        return name;
    }
    /**
     * 设置：真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取：真实姓名
     */
    public String getRealName() {
        return realName;
    }
    /**
     * 设置：备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取：备注
     */
    public String getRemarks() {
        return remarks;
    }
    /**
     * 设置：配送时间
     */
    public void setPickNumber(String pickNumber) {
        this.pickNumber = pickNumber;
    }

    /**
     * 获取：配送时间
     */
    public String getPickNumber() {
        return pickNumber;
    }
    /**
     * 设置：
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取：
     */
    public Integer getOrderId() {
        return orderId;
    }
    /**
     * 设置：快递单号
     */
    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    /**
     * 获取：快递单号
     */
    public String getExpressNo() {
        return expressNo;
    }
}
