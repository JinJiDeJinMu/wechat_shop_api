package com.chundengtai.base.entity;

import java.io.Serializable;


/**
 * 评价图片实体
 * 表名 nideshop_comment_picture
 *
 * @date 2017-08-29 14:45:55
 */
public class CommentPictureEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Integer pictureId;
    //评价Id
    private Integer commentId;
    //评价图片
    private String picUrl;
    //排序
    private Integer sortOrder;

    private Integer status;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.pictureId = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return pictureId;
    }

    /**
     * 设置：评价Id
     */
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    /**
     * 获取：评价Id
     */
    public Integer getCommentId() {
        return commentId;
    }

    /**
     * 设置：评价图片
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    /**
     * 获取：评价图片
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * 设置：排序
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * 获取：排序
     */
    public Integer getSortOrder() {
        return sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
