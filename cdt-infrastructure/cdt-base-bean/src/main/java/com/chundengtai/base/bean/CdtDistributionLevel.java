package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 分销层级表 分销层级表-绑定用户关系
 * </p>
 *
 * @author Royal
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtDistributionLevel implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @TableId(value = "id" , type = IdType.AUTO)
  private Long id;

  /**
   * 用户id
   */
  private Integer userId;

  /**
   * 用户所赚的钱
   */
  private BigDecimal money;

  /**
   * 推荐人id
   */
  private Integer parentId;

  /**
   * 分销层级
   */
  private Integer fxLevel;

  private Integer groupId;

  /**
   * 顺序标号 记录上级发展的下线标号
   */
  private Integer devNum;

  /**
   * 是否有效成交 0-false,1-true
   */
  private Integer isTrade;

  /**
   * 成功订单数
   */
  private Integer tradeOrderNum;

  /**
   * 发起人id
   */
  private Integer sponsorId;

  /**
   * 创建时间
   */
  private Date createdTime;

  /**
   * 校验token
   */
  private String token;


}
