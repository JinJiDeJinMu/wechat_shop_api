package com.chundengtai.base.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 分销记录表
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtRebateLog implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 获佣金用户id
     */
    private Integer goldUserId;

    /**
     * 购买人用户id
     */
    private Integer buyUserId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 商品总价格
     */
    private BigDecimal goodsPrice;

    /**
     * 佣金价格
     */
    private BigDecimal money;

    /**
     * 获佣用户级别
     */
    private Integer level;

    /**
     * 完成时间
     */
    private String completeTime;

    /**
     * 该订单的状态
     */
    private String status;

    /**
     * 确定分成或者取消时间
     */
    private LocalDateTime confirmTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商铺id
     */
    private Long mechantId;

    /**
     * 系统校验token
     */
    private String token;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;


}
