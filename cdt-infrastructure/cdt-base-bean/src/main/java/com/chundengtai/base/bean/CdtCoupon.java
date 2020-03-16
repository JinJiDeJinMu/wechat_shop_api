package com.chundengtai.base.bean;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-03-09
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class CdtCoupon implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String name;

      /**
     * 0：满减，1折扣，2，抵用
     */
      private Integer type;

      /**
     * 适用范围0->全场通用；1->指定分类；2->指定商品
     */
      private Integer useType;

      /**
     * 每个人限领个数
     */
      private Integer perLimit;

      /**
     * 时间类型，0是活动开始到活动结束，1是领取多少天之内
     */
      private Integer timeType;

    private Date startDate;

    private Date endDate;

    private Integer days;

      /**
     * 发放总数量
     */
      private Integer totalCount;

      /**
     * 已使用数量
     */
      private Integer useCount;

      /**
     * 领取数量
     */
      private Integer receiveCount;

      /**
     * 可领取的会员类型：0->无限制'`1会员
     */
      private Integer memberLevel;

      /**
     * 满多少
     */
      private BigDecimal fullMoney;

      /**
     * 减多少
     */
      private BigDecimal reduceMoney;

      /**
     * 折扣
     */
      private BigDecimal discount;

      /**
     * 抵现金
     */
      private BigDecimal offsetMoney;

      /**
     * 商户id
     */
      private Integer merchantId;


}
