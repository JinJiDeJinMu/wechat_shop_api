package com.platform.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "评论请求参数类", description = "评论请求参数类")
public class CommentReq implements Serializable {

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "商品Id")
    private Integer goodId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论星级")
    private Integer starLevel;
}
