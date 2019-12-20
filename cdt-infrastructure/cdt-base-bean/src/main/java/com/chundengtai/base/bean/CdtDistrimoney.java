package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 分销比例
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cdt_distrimoney")
public class CdtDistrimoney implements Serializable {

    private static final long serialVersionUID=1L;

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
     * 二级合伙人比例
     */
    private BigDecimal secondPartner;

    /**
     * 三级合伙人比例
     */
    private BigDecimal thirdPartner;

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
    private Date createdTime;

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
