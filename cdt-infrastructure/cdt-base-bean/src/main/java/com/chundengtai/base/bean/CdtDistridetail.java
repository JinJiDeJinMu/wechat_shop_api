package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户购买分销得钱记录 用户购买分销得钱
 * </p>
 *
 * @author Royal
 * @since 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtDistridetail implements Serializable {

    private static final long serialVersionUID=1L;

  /**
   * id
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 用户的id
   */
  private Integer userId;

  /**
   * 得钱的用户id
   */
  private Integer goldUserId;

  /**
   * 订单号
   */
  private String orderSn;

  /**
   * 得到的钱
   */
  private BigDecimal money;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 校验token
   */
  private String token;

  /**
   * 创建时间
   */
  private LocalDateTime createdTime;

  /**
   * 更新时间
   */
  private LocalDateTime updateTime;


}
