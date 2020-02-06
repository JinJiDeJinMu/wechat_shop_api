package com.chundengtai.base.bean;

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
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtUserScore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 总积分
     */
    private BigDecimal score;

    /**
     * 历史总积分
     */
    private BigDecimal totalScore;

    private Integer level;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 加密
     */
    private String token;


}
