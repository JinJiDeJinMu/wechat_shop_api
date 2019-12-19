package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 分销层级表 分销层级表-绑定用户关系
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
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
    private LocalDateTime createdTime;

    /**
     * 校验token
     */
    private String token;


}
