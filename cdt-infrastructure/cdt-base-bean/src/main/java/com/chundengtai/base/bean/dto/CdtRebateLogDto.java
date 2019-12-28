package com.chundengtai.base.bean.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
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
 * 分销记录表 分销记录表
 * </p>
 *
 * @author Royal
 * @since 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtRebateLogDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 获佣金用户id
     */
    private Integer goldUserId;

    /**
     * 购买人用户id
     */
    private Integer buyUserId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 商品总价格
     */
    private BigDecimal goodsPrice;

    /**
     * 佣金价格
     */
    private BigDecimal money;

    /**
     * 获佣用户级别
     */
    private Integer level;

    /**
     * 完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completeTime;

    /**
     * 该订单的状态
     */
    private Integer status;

    public String getStatusText() {
        return OrderStatusEnum.getEnumByKey(this.status).getDesc();
    }

    /**
     * 该订单的状态
     */
    private String statusText;
    /**
     * 确定分成或者取消时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商铺id
     */
    private Long mechantId;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;


}
