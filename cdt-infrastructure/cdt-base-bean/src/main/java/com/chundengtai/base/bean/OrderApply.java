package com.chundengtai.base.bean;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2020-02-10
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("nideshop_order_apply")
public class OrderApply implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 提交审核订单ID
     */
        private Integer id;

      /**
     * 订单号
     */
      private String orderSn;

      /**
     * 商户id
     */
      private Long merchantId;

      /**
     * 商户名称
     */
      private String merchantName;

      /**
     * 金额
     */
      private BigDecimal money;

      /**
     * 提现ID
     */
      private Long userId;

      /**
     * 微信号
     */
      private String wxopenId;

      /**
     * 提现名称
     */
      private String userName;

      /**
     * 申请时间
     */
      private Date applyTime;

      /**
     * 审核时间
     */
      private Date endTime;

      /**
     * 默认是0：申请，1：审核通过，2：驳回，3:其他
     */
      private Integer status;

      /**
     * 备注
     */
      private String remarks;


}
