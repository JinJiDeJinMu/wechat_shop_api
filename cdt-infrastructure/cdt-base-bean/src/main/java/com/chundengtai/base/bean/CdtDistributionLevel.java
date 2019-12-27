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
 * 分销层级表 分销层级表-绑定用户关系
 * </p>
 *
 * @author Royal
 * @since 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtDistributionLevel implements Serializable {

    private static final long serialVersionUID=1L;

  /**
   * id
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 用户id
   */
  private Integer userId;

  /**
   * 推荐人id
   */
  private Integer parentId;

  /**
   * 分销层级
   */
  private Integer fxLevel;

  /**
   * 发起人id
   */
  private Integer sponsorId;

  /**
   * 创建时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdTime;

  /**
   * 校验token
   */
  @JSONField(serialize = false)
  private String token;


}
