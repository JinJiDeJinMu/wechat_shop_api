package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Royal
 * @since 2020-02-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("nideshop_goods_carousel")
public class GoodsCarousel implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private Integer goodsId;

    private Blob imgUrl;

    private Integer sortOrder;


}
