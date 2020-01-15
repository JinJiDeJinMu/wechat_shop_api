package com.chundengtai.base.entity;

import java.io.Serializable;

public class AttributeCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //
    private String name;
    //
    private Integer enabled;

    private Integer showStyle;
    private String bannerUrl;
    private Integer sortOrder;
    private Integer showPosition;

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

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getShowPosition() {
        return showPosition;
    }

    public void setShowPosition(Integer showPosition) {
        this.showPosition = showPosition;
    }

    public Integer getShowStyle() {
        return showStyle;
    }

    public void setShowStyle(Integer showStyle) {
        this.showStyle = showStyle;
    }

    /**
     * 设置：
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取：
     */
    public Integer getEnabled() {
        return enabled;
    }
}
