package com.chundengtai.base.bean;

import java.util.Date;
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
 * @since 2020-03-16
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class CdtUserCoupon implements Serializable {

    private static final long serialVersionUID=1L;

      private Integer couponId;

    private Integer userId;

    private Date userTime;

    private Integer number;

    private Integer status;

}
