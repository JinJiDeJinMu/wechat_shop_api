package com.chundengtai.base.entity;

import java.io.Serializable;


/**
 * @date 2017-08-15 08:03:40
 */
public class CommentPictureVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Long id;
    //评价Id
    private Long comment_id;
    //类型
    private Integer type;
    //评价图片
    private String pic_url;
    //排序
    private Integer sort_order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public Integer getSort_order() {
        return sort_order;
    }

    public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }
}
