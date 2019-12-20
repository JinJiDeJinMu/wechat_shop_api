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
 * 用户购买分销得钱记录
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cdt_distridetail")
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
     * 校验token
     */
    private String token;

    /**
     * 创建时间
     */
    private Date createdTime;


}
