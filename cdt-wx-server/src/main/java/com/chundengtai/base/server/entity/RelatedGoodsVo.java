package com.chundengtai.base.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:41
 */
@TableName("nideshop_related_goods")
public class RelatedGoodsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //商品Id
    private Integer goods_id;
    //关联商品id
    private Integer related_goods_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getRelated_goods_id() {
        return related_goods_id;
    }

    public void setRelated_goods_id(Integer related_goods_id) {
        this.related_goods_id = related_goods_id;
    }
}
