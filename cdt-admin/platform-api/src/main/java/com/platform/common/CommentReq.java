package com.platform.common;

import com.platform.entity.CommentPictureVo;
import com.platform.entity.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "评价请求参数类", description = "评价请求参数类")
public class CommentReq implements Serializable {

    @ApiModelProperty(value = "订单id")
    private String id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "商品Id")
    private Integer goodId;

    @ApiModelProperty(value = "评价内容")
    private String content;

    @ApiModelProperty(value = "评价星级")
    private Integer starLevel;

    @ApiModelProperty(value = "评价人")
    private Long userId;

    @ApiModelProperty(value = "评价时间")
    private Long commentTime;

    @ApiModelProperty(value = "评论人信息")
    private UserVo userInfo;

    @ApiModelProperty(value = "评论图片列表")
    private List<CommentPictureVo> commentPictureList;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long commentId) {
        this.userId = commentId;
    }

    public Long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Long commentTime) {
        this.commentTime = commentTime;
    }

    public UserVo getUserVo() {
        return userInfo;
    }

    public void setUserVo(UserVo userVo) {
        this.userInfo = userVo;
    }

    public List<CommentPictureVo> getCommentPictureList() {
        return commentPictureList;
    }

    public void setCommentPictureList(List<CommentPictureVo> commentPictureList) {
        this.commentPictureList = commentPictureList;
    }
}
