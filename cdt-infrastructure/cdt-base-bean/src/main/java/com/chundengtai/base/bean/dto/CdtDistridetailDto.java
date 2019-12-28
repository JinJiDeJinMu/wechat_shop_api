package com.chundengtai.base.bean.dto;

import com.chundengtai.base.weixinapi.DistributionStatus;
import com.chundengtai.base.weixinapi.DistributionTypeEnum;
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
 * 用户购买分销得钱记录 用户购买分销得钱
 * </p>
 *
 * @author Royal
 * @since 2019-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtDistridetailDto implements Serializable {

  /**
   * id
   */
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
   * 创建时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdTime;

  /**
   * 更新时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updateTime;

  /**
   * 0分销返佣,1为合伙人
   */
  private Integer type;

  public String getTypeName() {
    if (this.type != null) {
      return DistributionTypeEnum.getEnumByKey(this.type).getDesc();
    }
    return "";
  }

  private String typeName;


  /**
   * 状态
   */
  private Integer status;

  public String getStatusText() {
    if (this.status != null) {
      return DistributionStatus.getEnumByKey(this.status).getDesc();
    }
    return "";
  }

  private String statusText;


}
