package com.chundengtai.base.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * @date 2017-08-15 08:03:39
 */
public class CollectVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //用户Id
    private Long user_id;
    //产品Id
    private Integer good_id;
    //添加时间
    private Date add_time;
    //是否是关注
    private Integer is_attention;
    //
    private Integer type_id;
    //
    private String name;
    private String list_pic_url;
    private String goods_brief;
    private String retail_price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setGood_id(Integer good_id) {
        this.good_id = good_id;
    }

    public Integer getGood_id() {
        return good_id;
    }

    public void setAdd_time(Date add_time) {
        this.add_time = add_time;
    }

    public Date getAdd_time() {
        return add_time;
    }

    public Integer getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(Integer is_attention) {
        this.is_attention = is_attention;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getList_pic_url() {
        return list_pic_url;
    }

    public void setList_pic_url(String list_pic_url) {
        this.list_pic_url = list_pic_url;
    }

    public String getGoods_brief() {
        return goods_brief;
    }

    public void setGoods_brief(String goods_brief) {
        this.goods_brief = goods_brief;
    }

    public String getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(String retail_price) {
        this.retail_price = retail_price;
    }
}
