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
     * 未结算
     */
    private BigDecimal unbalanced;

    /**
     * 下线人数
     */
    private Integer statsPerson;

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
