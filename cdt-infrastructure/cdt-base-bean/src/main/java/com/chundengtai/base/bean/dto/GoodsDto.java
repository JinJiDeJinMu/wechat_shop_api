package com.chundengtai.base.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 *
 */
@Data
@NoArgsConstructor
@ApiModel(value = "产品实体dto", description = "产品实体dto类")
public class GoodsDto implements Serializable {

    @ApiModelProperty(name = "id", value = "评论主键id", dataType = "int")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "浏览量")
    private Integer browse;

    @ApiModelProperty(value = "简明介绍")
    private String goodsBrief;

    //添加时间
    @ApiModelProperty(value = "添加时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;

    @ApiModelProperty(value = "商品主图")
    private String primaryPicUrl;

    //商品列表图
    @ApiModelProperty(value = "商品列表图")
    private String listPicUrl;

    //市场价
    @ApiModelProperty(value = "市场价格")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "零售价格")
    private BigDecimal retailPrice;

    //销售量
//    @ApiModelProperty(value = "销售量")
//    private Integer sell_volume;

    //推广描述
    @ApiModelProperty(value = "推广描述")
    private String promotionDesc;


}
