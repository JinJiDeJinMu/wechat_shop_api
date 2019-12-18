package com.platform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家实体
 * 表名 cdt_merchant
 *
 * @author lipengjun
 * @date 2019-11-15 17:08:05
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class CdtMerchantEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    private Long id;
    private String key;
    /**
     * 名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一
     */
    private String shopName;
    /**
     * 用户id
     */
    private Integer userId;
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
     * 商家是否提现状态
     */
    private Integer cashStatus;
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

    @Override
    public String toString() {
        return "CdtMerchantEntity{" +
                "id=" + id +
                ", shopName='" + shopName + '\'' +
                ", userId=" + userId +
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
                ", cashStatus=" + cashStatus +
                ", transportType=" + transportType +
                ", fixedFreight=" + fixedFreight +
                ", fullFreeShipping=" + fullFreeShipping +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDistribution=" + isDistribution +
                '}';
    }
}
