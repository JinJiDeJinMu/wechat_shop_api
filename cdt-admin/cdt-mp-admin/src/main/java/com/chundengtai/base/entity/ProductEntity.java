package com.chundengtai.base.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //商品Id
    private Integer goodsId;
    //商品规格ids
    private String goodsSpecificationIds;
    //商品序列号
    private String goodsSn;
    //商品库存
    private Integer goodsNumber;
    //零售价格
    private BigDecimal retailPrice;
    //市场价格
    private BigDecimal marketPrice;
    //商户ID
    private long merchant_id;

    //商品
    private String goodsName;
    private String specificationValue;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    private String picUrl;
    private BigDecimal groupPrice;//团购价格(元)

    public BigDecimal getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(BigDecimal groupPrice) {
        this.groupPrice = groupPrice;
    }

    public String getSpecificationValue() {
        return specificationValue;
    }

    public void setSpecificationValue(String specificationValue) {
        this.specificationValue = specificationValue;
    }

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：商品Id
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取：商品Id
     */
    public Integer getGoodsId() {
        return goodsId;
    }

    /**
     * 设置：商品规格ids
     */
    public void setGoodsSpecificationIds(String goodsSpecificationIds) {
        this.goodsSpecificationIds = goodsSpecificationIds;
    }

    /**
     * 获取：商品规格ids
     */
    public String getGoodsSpecificationIds() {
        return goodsSpecificationIds;
    }

    /**
     * 设置：商品序列号
     */
    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    /**
     * 获取：商品序列号
     */
    public String getGoodsSn() {
        return goodsSn;
    }

    /**
     * 设置：商品库存
     */
    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    /**
     * 获取：商品库存
     */
    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    /**
     * 设置：零售价格
     */
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    /**
     * 获取：零售价格
     */
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public long getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(long merchant_id) {
        this.merchant_id = merchant_id;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", goodsSpecificationIds='" + goodsSpecificationIds + '\'' +
                ", goodsSn='" + goodsSn + '\'' +
                ", goodsNumber=" + goodsNumber +
                ", retailPrice=" + retailPrice +
                ", marketPrice=" + marketPrice +
                ", merchant_id=" + merchant_id +
                ", goodsName='" + goodsName + '\'' +
                ", specificationValue='" + specificationValue + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", groupPrice=" + groupPrice +
                '}';
    }
}
