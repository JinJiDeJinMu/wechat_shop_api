package com.platform.entity;

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
    private Long id;

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

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "评论人信息")
    private UserVo userInfo;

    @ApiModelProperty(value = "评论图片列表")
    private List<CommentPictureVo> commentPictureList;


}
