package com.platform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BuyGoodsVo implements Serializable {
    private Integer goodsId;
    private Integer productId;
    private Integer number;
    private String name;
    private String skuName;
    private Integer goodsType;

}
