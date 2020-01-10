package com.chundengtai.base.entity;

import java.io.Serializable;

/**
 * 回复评论表实体
 * 表名 rep_comment
 *
 * @date 2019-11-18 19:35:15
 */
public class RepCommentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * 评价id
     */
    private Integer commentId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户name
     */
    private String userName;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 时间
     */
    private Long addTime;

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
     * 设置：评价id
     */
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    /**
     * 获取：评价id
     */
    public Integer getCommentId() {
        return commentId;
    }

    /**
     * 设置：用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置：用户name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取：用户name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置：评论内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取：评论内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置：时间
     */
    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取：时间
     */
    public Long getAddTime() {
        return addTime;
    }
}
