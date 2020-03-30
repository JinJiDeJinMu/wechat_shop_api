package com.chundengtai.base.bean.dto;

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
 * @since 2020-03-16
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class CdtUserCouponDao implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer couponId;

    private Integer userId;

    private Date userTime;

    private Date endTime;

    private Integer number;

    private Integer status;

    private String nickName;

    private Integer id;

    private String name;

    private Integer type;


    private Integer useType;


    private Integer perLimit;


    private Integer timeType;

    private Date startDate;

    private Date endDate;

    private Integer days;

    private Integer totalCount;


    private Integer useCount;


    private Integer receiveCount;


    private Integer memberLevel;


    private BigDecimal fullMoney;


    private BigDecimal reduceMoney;


    private BigDecimal discount;


    private BigDecimal offsetMoney;


    private Integer merchantId;

    private String imgUrl;




}
