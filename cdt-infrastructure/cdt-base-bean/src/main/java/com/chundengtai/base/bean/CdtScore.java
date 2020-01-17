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
 *
 * </p>
 *
 * @author Royal
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtScore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 积分数
     */
    private Long score;

    /**
     * 描述
     */
    private String detail;

    /**
     * 积分价钱
     */
    private BigDecimal money;

    /**
     * 抵消金额
     */
    private BigDecimal offsetMoney;

    /**
     * 类型，0：正常，1停用
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;


}
