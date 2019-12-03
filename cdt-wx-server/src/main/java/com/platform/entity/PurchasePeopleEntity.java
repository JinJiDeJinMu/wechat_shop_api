package com.platform.entity;

import java.io.Serializable;
/**
 * 实体
 * 表名 nideshop_purchase_people
 *
 * @author lipengjun
 * @date 2019-11-25 09:39:01
 */
public class PurchasePeopleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;
    /**
     * 商品id
     */
    private Integer goodsId;
    /**
     * 
     */
    private Integer userId;
    /**
     * 规格ID
     */
    private Integer specificationsId;

    /**
     * 翻译头像
     */
    private String avatar;


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
     * 设置：商品id
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取：商品id
     */
    public Integer getGoodsId() {
        return goodsId;
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
     * 设置：规格ID
     */
    public void setSpecificationsId(Integer specificationsId) {
        this.specificationsId = specificationsId;
    }

    /**
     * 获取：规格ID
     */
    public Integer getSpecificationsId() {
        return specificationsId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
