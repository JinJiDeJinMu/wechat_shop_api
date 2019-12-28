package com.chundengtai.base.bean;

import java.math.BigDecimal;
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
 * @since 2019-12-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class CdtDistridetailApply implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * id
     */
        private Integer id;

      /**
     * 订单号
     */
      private String orderSn;

      /**
     * 返利金额
     */
      private BigDecimal money;

      /**
     * 提现账号id
     */
      private Integer advanceId;

      /**
     * 账号真实姓名
     */
      private String advanceName;

      /**
     * 申请时间
     */
      private Date applyTime;

      /**
     * 申请人
     */
      private Integer applyId;

      /**
     * 更新时间
     */
      private Date updateTime;

      /**
     * 更新人
     */
      private Integer updateId;

      /**
     * 状态
     */
      private Integer status;


}
