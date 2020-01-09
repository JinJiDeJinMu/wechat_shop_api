package com.platform.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评价表实体
 * 表名 nideshop_comment_v2
 *
 * @date 2019-11-26 11:27:34
 */
public class CommentV2Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * 订单id
     */
    private Integer orderNo;
    /**
     * 商品id
     */
    private Integer goodId;
    /**
     * 评价内容
     */
    private String content;
    /**
     * 添加时间
     */
    private Long commentTime;
    /**
     * 等级
     */
    private Integer starLevel;
    /**
     * 评论人
     */
    private Integer userId;

    private Date createTime;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 评论图片列表
     */

    private List<CommentPictureEntity> commentPictureEntities;


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
     * 设置：订单id
     */
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取：订单id
     */
    public Integer getOrderNo() {
        return orderNo;
    }

    /**
     * 设置：商品id
     */
    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    /**
     * 获取：商品id
     */
    public Integer getGoodId() {
        return goodId;
    }

    /**
     * 设置：评价内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取：评价内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置：添加时间
     */
    public void setCommentTime(Long commentTime) {
        this.commentTime = commentTime;
    }

    /**
     * 获取：添加时间
     */
    public Long getCommentTime() {
        return commentTime;
    }

    /**
     * 设置：等级
     */
    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }

    /**
     * 获取：等级
     */
    public Integer getStarLevel() {
        return starLevel;
    }

    /**
     * 设置：评论人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：评论人
     */
    public Integer getUserId() {
        return userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CommentPictureEntity> getCommentPictureEntities() {
        return commentPictureEntities;
    }

    public void setCommentPictureEntities(List<CommentPictureEntity> commentPictureEntities) {
        this.commentPictureEntities = commentPictureEntities;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
