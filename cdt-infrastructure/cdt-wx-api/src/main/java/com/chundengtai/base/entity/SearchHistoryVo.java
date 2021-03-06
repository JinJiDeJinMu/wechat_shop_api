package com.chundengtai.base.entity;

import java.io.Serializable;


/**
 * @date 2017-08-15 08:03:41
 */
public class SearchHistoryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //关键字
    private String keyword;
    //搜索来源，如PC、小程序、APP等
    private String from;
    //搜索时间
    private Long add_time;
    //会员Id
    private String user_id;

    private Integer status;

    private String goods;

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Long add_time) {
        this.add_time = add_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
