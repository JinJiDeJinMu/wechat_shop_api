package com.chundengtai.base.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户分布
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtUserDistribution implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 分销会员id
     */
    private Integer userId;

    /**
     * user_name
     */
    private String userName;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 购物车id
     */
    private Integer cartId;

    /**
     * 分享次数
     */
    private Integer shareNum;

    /**
     * 分销销量
     */
    private Integer salesNum;

    /**
     * store_id
     */
    private Long storeId;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 校验
     */
    private String token;


}
