package com.chundengtai.base.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户分布 用户分布
 * </p>
 *
 * @author Royal
 * @since 2019-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtUserDistribution implements Serializable {

    private static final long serialVersionUID=1L;

  /**
   * id
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 分销会员id
   */
  private Integer userId;

  /**
   * user_name
   */
  private String userName;

  /**
   * 商品id
   */
  private Integer goodsId;

  /**
   * 商品名称
   */
  private String goodsName;

  /**
   * 规格id
   */
  private Integer specId;

  /**
   * 分享次数
   */
  private Integer shareNum;

  /**
   * 分销销量
   */
  private Integer salesNum;

  /**
   * 商家id
   */
  private Long mechantId;

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

  /**
   * 校验
   */
  @JSONField(serialize = false)
  private String token;


}
