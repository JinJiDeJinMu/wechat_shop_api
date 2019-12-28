package com.chundengtai.base.bean.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 分销用户汇总信息
 * </p>
 *
 * @author Royal
 * @since 2019-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtUserSummaryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 累计收益
     */
    private BigDecimal totalIncome;

    /**
     * unbalanced
     */
    private BigDecimal unbalanced;

    /**
     * 下线人数
     */
    private Integer statsPerson;

    /**
     * 分销订单数量
     */
    private Integer shareOrderNum;

    /**
     * 合伙人奖励收益
     */
    private BigDecimal partnerIncome;

    /**
     * 下线累计收益
     */
    private BigDecimal statsIncome;

    /**
     * 加速合伙人
     */
    private Integer speedPartner;

    /**
     * 创建时间
     */
    private Date createdTime;


}
