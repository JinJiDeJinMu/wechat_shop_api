
package com.chundengtai.base.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductItemDto implements Serializable {

    @ApiModelProperty(value = "产品名称", required = true)
    private String prodName;

    @ApiModelProperty(value = "产品个数", required = true)
    private Integer prodCount;

    @ApiModelProperty(value = "产品图片路径", required = true)
    private String pic;

    @ApiModelProperty(value = "产品价格", required = true)
    private Double price;

    @ApiModelProperty(value = "商品总金额", required = true)
    private Double productTotalAmount;

    @ApiModelProperty(value = "产品ID", required = true)
    private Long prodId;

    @ApiModelProperty(value = "skuId", required = true)
    private Long skuId;

    @ApiModelProperty(value = "规格名称", required = true)
    private String skuName;

    @ApiModelProperty(value = "basketId", required = true)
    private Long basketId;

    @ApiModelProperty(value = "商品实际金额 = 商品总金额 - 分摊的优惠金额")
    private Double actualTotal;

    @ApiModelProperty(value = "满减满折优惠id，0不主动参与活动（用户没有主动参与该活动），-1主动不参与活动")
    private Long discountId = 0L;

    @ApiModelProperty(value = "分摊的优惠金额")
    private Double shareReduce;

    @ApiModelProperty("参与满减活动列表")
    private List<DiscountDto> discounts = new ArrayList<>();
}
