package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家实体
 * 表名 cdt_merchant
 *
 * @date 2019-11-15 17:08:05
 */
public class CdtMerchantEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    private Long id;
    /**
     * 名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一
     */
    private String shopName;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 类型
     */
    private Integer shopType;
    /**
     * 简介(可修改)
     */
    private String intro;
    /**
     * 公告(可修改)
     */
    private String shopNotice;
    /**
     * 店铺行业(餐饮、生鲜果蔬、鲜花等)
     */
    private Integer shopIndustry;
    /**
     * 店长
     */
    private String shopOwner;
    /**
     * 店铺绑定的手机(登录账号：唯一)
     */
    private String mobile;
    /**
     * 店铺联系电话
     */
    private String tel;
    /**
     * 店铺所在纬度(可修改)
     */
    private String shopLat;
    /**
     * 店铺所在经度(可修改)
     */
    private String shopLng;
    /**
     * 店铺详细地址
     */
    private String shopAddress;
    /**
     * 店铺所在省份（描述）
     */
    private String province;
    /**
     * 店铺所在城市（描述）
     */
    private String city;
    /**
     * 店铺所在区域（描述）
     */
    private String area;
    /**
     * 店铺省市区代码，用于回显
     */
    private String pcaCode;
    /**
     * 店铺logo(可修改)
     */
    private String shopLogo;
    /**
     * 店铺相册
     */
    private String shopPhotos;
    /**
     * 每天营业时间段(可修改)
     */
    private String openTime;
    /**
     * 店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改
     */
    private Integer shopStatus;
    /**
     * 0:商家承担运费; 1:买家承担运费
     */
    private Integer transportType;
    /**
     * 固定运费
     */
    private BigDecimal fixedFreight;
    /**
     * 满X包邮
     */
    private BigDecimal fullFreeShipping;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 分销开关(0:开启 1:关闭)
     */
    private Integer isDistribution;

    /**
     * 设置：商家id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：商家id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 获取：名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 设置：用户id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置：类型
     */
    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    /**
     * 获取：类型
     */
    public Integer getShopType() {
        return shopType;
    }

    /**
     * 设置：简介(可修改)
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * 获取：简介(可修改)
     */
    public String getIntro() {
        return intro;
    }

    /**
     * 设置：公告(可修改)
     */
    public void setShopNotice(String shopNotice) {
        this.shopNotice = shopNotice;
    }

    /**
     * 获取：公告(可修改)
     */
    public String getShopNotice() {
        return shopNotice;
    }

    /**
     * 设置：店铺行业(餐饮、生鲜果蔬、鲜花等)
     */
    public void setShopIndustry(Integer shopIndustry) {
        this.shopIndustry = shopIndustry;
    }

    /**
     * 获取：店铺行业(餐饮、生鲜果蔬、鲜花等)
     */
    public Integer getShopIndustry() {
        return shopIndustry;
    }

    /**
     * 设置：店长
     */
    public void setShopOwner(String shopOwner) {
        this.shopOwner = shopOwner;
    }

    /**
     * 获取：店长
     */
    public String getShopOwner() {
        return shopOwner;
    }

    /**
     * 设置：店铺绑定的手机(登录账号：唯一)
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取：店铺绑定的手机(登录账号：唯一)
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置：店铺联系电话
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取：店铺联系电话
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置：店铺所在纬度(可修改)
     */
    public void setShopLat(String shopLat) {
        this.shopLat = shopLat;
    }

    /**
     * 获取：店铺所在纬度(可修改)
     */
    public String getShopLat() {
        return shopLat;
    }

    /**
     * 设置：店铺所在经度(可修改)
     */
    public void setShopLng(String shopLng) {
        this.shopLng = shopLng;
    }

    /**
     * 获取：店铺所在经度(可修改)
     */
    public String getShopLng() {
        return shopLng;
    }

    /**
     * 设置：店铺详细地址
     */
    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    /**
     * 获取：店铺详细地址
     */
    public String getShopAddress() {
        return shopAddress;
    }

    /**
     * 设置：店铺所在省份（描述）
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取：店铺所在省份（描述）
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置：店铺所在城市（描述）
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取：店铺所在城市（描述）
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置：店铺所在区域（描述）
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取：店铺所在区域（描述）
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置：店铺省市区代码，用于回显
     */
    public void setPcaCode(String pcaCode) {
        this.pcaCode = pcaCode;
    }

    /**
     * 获取：店铺省市区代码，用于回显
     */
    public String getPcaCode() {
        return pcaCode;
    }

    /**
     * 设置：店铺logo(可修改)
     */
    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    /**
     * 获取：店铺logo(可修改)
     */
    public String getShopLogo() {
        return shopLogo;
    }

    /**
     * 设置：店铺相册
     */
    public void setShopPhotos(String shopPhotos) {
        this.shopPhotos = shopPhotos;
    }

    /**
     * 获取：店铺相册
     */
    public String getShopPhotos() {
        return shopPhotos;
    }

    /**
     * 设置：每天营业时间段(可修改)
     */
    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    /**
     * 获取：每天营业时间段(可修改)
     */
    public String getOpenTime() {
        return openTime;
    }

    /**
     * 设置：店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改
     */
    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    /**
     * 获取：店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改
     */
    public Integer getShopStatus() {
        return shopStatus;
    }

    /**
     * 设置：0:商家承担运费; 1:买家承担运费
     */
    public void setTransportType(Integer transportType) {
        this.transportType = transportType;
    }

    /**
     * 获取：0:商家承担运费; 1:买家承担运费
     */
    public Integer getTransportType() {
        return transportType;
    }

    /**
     * 设置：固定运费
     */
    public void setFixedFreight(BigDecimal fixedFreight) {
        this.fixedFreight = fixedFreight;
    }

    /**
     * 获取：固定运费
     */
    public BigDecimal getFixedFreight() {
        return fixedFreight;
    }

    /**
     * 设置：满X包邮
     */
    public void setFullFreeShipping(BigDecimal fullFreeShipping) {
        this.fullFreeShipping = fullFreeShipping;
    }

    /**
     * 获取：满X包邮
     */
    public BigDecimal getFullFreeShipping() {
        return fullFreeShipping;
    }

    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置：分销开关(0:开启 1:关闭)
     */
    public void setIsDistribution(Integer isDistribution) {
        this.isDistribution = isDistribution;
    }

    /**
     * 获取：分销开关(0:开启 1:关闭)
     */
    public Integer getIsDistribution() {
        return isDistribution;
    }

    @Override
    public String toString() {
        return "CdtMerchantEntity{" +
                "id=" + id +
                ", shopName='" + shopName + '\'' +
                ", userId='" + userId + '\'' +
                ", shopType=" + shopType +
                ", intro='" + intro + '\'' +
                ", shopNotice='" + shopNotice + '\'' +
                ", shopIndustry=" + shopIndustry +
                ", shopOwner='" + shopOwner + '\'' +
                ", mobile='" + mobile + '\'' +
                ", tel='" + tel + '\'' +
                ", shopLat='" + shopLat + '\'' +
                ", shopLng='" + shopLng + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", pcaCode='" + pcaCode + '\'' +
                ", shopLogo='" + shopLogo + '\'' +
                ", shopPhotos='" + shopPhotos + '\'' +
                ", openTime='" + openTime + '\'' +
                ", shopStatus=" + shopStatus +
                ", transportType=" + transportType +
                ", fixedFreight=" + fixedFreight +
                ", fullFreeShipping=" + fullFreeShipping +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDistribution=" + isDistribution +
                '}';
    }
}
