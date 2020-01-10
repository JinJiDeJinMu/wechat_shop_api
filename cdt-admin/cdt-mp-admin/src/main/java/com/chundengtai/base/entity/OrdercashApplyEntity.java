package com.chundengtai.base.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 * 表名 ordercash_apply
 *
 * @date 2019-12-11 11:29:38
 */
public class OrdercashApplyEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 提交审核订单ID
     */
    private Integer orderId;
    /**
     *
     */
    private String orderSn;
    /**
     *
     */
    private Long merchantId;

    private String merchantName;

    /**
     * 订单实际支付金额
     */
    private BigDecimal actualPrice;
    /**
     * 订单实际支付时间
     */
    private Date payTime;
    /**
     * 申请人ID
     */
    private Long applyId;

    private String applyName;
    /**
     *
     */
    private Date applyTime;
    /**
     *
     */
    private Date endTime;
    /**
     * 默认是0：申请，1：审核通过，2：驳回，3:其他
     */
    private Integer status;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 审核操作人
     */
    private Long operator;

    private String operatorName;

    /**
     * 设置：提交审核订单ID
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取：提交审核订单ID
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置：
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * 获取：
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置：
     */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * 获取：
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * 设置：订单实际支付金额
     */
    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    /**
     * 获取：订单实际支付金额
     */
    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    /**
     * 设置：订单实际支付时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取：订单实际支付时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 设置：申请人ID
     */
    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    /**
     * 获取：申请人ID
     */
    public Long getApplyId() {
        return applyId;
    }

    /**
     * 设置：
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取：
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * 设置：
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取：
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置：默认是0：申请，1：审核通过，2：驳回，3:其他
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：默认是0：申请，1：审核通过，2：驳回，3:其他
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置：备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取：备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置：审核操作人
     */
    public void setOperator(Long operator) {
        this.operator = operator;
    }

    /**
     * 获取：审核操作人
     */
    public Long getOperator() {
        return operator;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
