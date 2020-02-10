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
 *
 * </p>
 *
 * @author Royal
 * @since 2019-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("nideshop_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orderSn;

    private Integer userId;

    /**
     * 订单状态:101订单已取消,102订单已删除,201订单已付款,等待发货,300订单已发货,301用户确认收货,401 没有发货退款,402 已收货退款退货
     */
    private Integer orderStatus;

    /**
     * 发货状态 商品配送情况;0未发货,1已发货,2已收货,4退货
     */
    private Integer shippingStatus;

    /**
     * 付款状态 支付状态;0未付款;1付款中;2已付款;4退款
     */
    private Integer payStatus;

    /**
     * 评价状态（0是未评价，1是评价）
     */
    private Integer evaluateStatus;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 街道
     */
    private String district;

    /**
     * 地址
     */
    private String address;

    /**
     * 收货手机
     */
    private String mobile;

    /**
     * 补充说明
     */
    private String postscript;

    /**
     * 快递公司Id
     */
    private Integer shippingId;

    /**
     * 快递公司名称
     */
    private String shippingName;

    /**
     * 付款
     */
    private String payId;

    private String payName;

    /**
     * 快递费用
     */
    private BigDecimal shippingFee;

    /**
     * 实际需要支付的金额
     */
    private BigDecimal actualPrice;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 积分抵扣金额
     */
    private BigDecimal integralMoney;

    /**
     * 订单总价
     */
    private BigDecimal orderPrice;

    /**
     * 商品总价
     */
    private BigDecimal goodsPrice;

    /**
     * 新增时间
     */
    private Date addTime;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 配送费用
     */
    private BigDecimal freightPrice;

    /**
     * 使用的优惠券id
     */
    private Integer couponId;

    private Integer parentId;

    /**
     * 优惠价格
     */
    private BigDecimal couponPrice;

    /**
     * 支付回调状态
     */
    private String callbackStatus;

    /**
     * 快递号单号
     */
    private String shippingNo;

    /**
     * 订单满减金额
     */
    private BigDecimal fullCutPrice;

    /**
     * 1购物车、2普通、3秒杀、4团购
     */
    private String orderType;

    /**
     * 品牌Id
     */
    private Integer brandId;

    /**
     * 单位分 应结订单金额 扣除手续后 到账金额
     */
    private Integer settlementTotalFee;

    /**
     * 总付款金额
     */
    private BigDecimal allPrice;

    /**
     * 总订单ID
     */
    private String allOrderId;

    /**
     * 推荐人id
     */
    private Integer promoterId;

    /**
     * 分佣金额
     */
    private BigDecimal brokerage;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 团购组ID
     */
    private String groupBuyingId;

    /**
     * 1
     */
    private Integer goodsType;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    /**
     * 第一个上级
     */
    private Integer firstLeader;

    /**
     * 第二个上级
     */
    private Integer secondLeader;

    /**
     * 体现状态
     */
    private Integer isApply;


}
