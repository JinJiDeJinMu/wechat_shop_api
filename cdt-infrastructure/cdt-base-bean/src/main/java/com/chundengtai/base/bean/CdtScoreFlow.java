package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Royal
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtScoreFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 流水号
     */
    private String flowSn;

    /**
     * 积分id
     */
    private Integer scoreId;

    /**
     * 积分总数
     */
    private Long scoreSum;

    /**
     * 充值金额
     */
    private BigDecimal money;

    /**
     * 抵消金额
     */
    private BigDecimal offsetMoney;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 加密
     */
    private String token;


}
