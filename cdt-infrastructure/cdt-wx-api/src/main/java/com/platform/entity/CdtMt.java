package com.platform.entity;

import lombok.Data;

@Data
public class CdtMt {

    private String shopOwner;

    private String phone;

    private Integer shoptype;

    private String shopname;

    private String address;

    public String getShopOwner() {
        return shopOwner;
    }

    public void setShopOwner(String shopOwner) {
        this.shopOwner = shopOwner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getShoptype() {
        return shoptype;
    }

    public void setShoptype(Integer shoptype) {
        this.shoptype = shoptype;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
