package com.chundengtai.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class GoodsAttributeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Integer id;
    //商品Id
    private Integer goodsId;
    //属性Id
    private Integer attributeId;
    //属性值
    private String value;

}
