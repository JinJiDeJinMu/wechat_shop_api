package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 分销比例 分销及合伙人分钱比例表
 * </p>
 *
 * @author Royal
 * @since 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtDistrimoney implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 一级得钱的比例
   */
  private BigDecimal firstPercent;

  /**
   * 二级得钱比例
   */
  private BigDecimal secondPercent;

  /**
   * 一级合伙人比例
   */
  private BigDecimal firstPartner;

  /**
   * 一级需要邀请的人数
   */
  private Integer firstPersonCondition;

  /**
   * 二级合伙人比例
   */
  private BigDecimal secondPartner;

  /**
   * 二级需要发展的合伙人数
   */
  private Integer secondPersonCondition;

  /**
   * 三级合伙人比例
   */
  private BigDecimal thirdPartner;

  /**
   * 三级需要产生的成交金额
   */
  private BigDecimal thirdTotalMoney;

  /**
   * 乐观锁
   */
  private Integer revision;

  /**
   * 状态 0,1
   */
  private Integer status;

  /**
   * 创建人
   */
  private String createdBy;

  /**
   * 创建时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  /**
   * 更新时间
   */
  private Date updatedTime;

  /**
   * 更新人
   */
  private String updatedBy;

  /**
   * 校验token 防篡改token校验
   */
  private String token;


}
