package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.TableField;
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
      @TableId(value = "id")
      private Long id;

      /**
     * 订单号
     */
      private String orderSn;

  /**
   * 分销记录状态
   */
  private Integer status;

      /**
     * 返利金额
     */
      private BigDecimal money;

      /**
     * 提现账号id
     */
      @TableField("weixinOpenid")
      private String weixinOpenid;

      /**
       * 提现人
     */
      @TableField("userName")
      private String userName;

  /**
   * 真实姓名
   */
  @TableField("realName")
  private String realName;

      /**
     * 申请时间
     */
      private Date applyTime;

      /**
       * 更新人
     */
      private String operator;

      /**
     * 更新时间
     */
      private Date updateTime;


}
