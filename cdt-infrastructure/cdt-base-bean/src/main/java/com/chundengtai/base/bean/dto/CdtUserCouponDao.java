package com.chundengtai.base.bean.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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

    private Integer number;

    private Integer status;

    private String nickName;

    private String couponName;

    private Integer couponType;



}
