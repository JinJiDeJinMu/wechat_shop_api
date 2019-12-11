package com.platform.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/* 实体
 * 表名 nideshop_ordercase_apply
 *
 * @author lipengjun
 * @date 2019-12-10 17:30:30
 */
public class OrdercaseApplyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;
    /**
     * 提交审核订单ID
     */
    private String orderId;
    /**
     * 
     */
    private Integer merchantId;
    /**
     * 
     */
    private Integer orderStatus;
    /**
     * 
     */
    private BigDecimal orderPrice;
    /**
     * 
     */
    private BigDecimal goodPrice;
    /**
     * 申请人ID
     */
    private Integer applyId;
    /**
     * 
     */
    private Date applyTime;
    /**
     * 
     */
    private Date endTime;
    /**
     * 默认是0：申请，1：审核通过，2：其他
     */
    private Integer status;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 审核操作人
     */
    private Integer operator;

    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：提交审核订单ID
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取：提交审核订单ID
     */
    public String getOrderId() {
        return orderId;
    }
    /**
     * 设置：
     */
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * 获取：
     */
    public Integer getMerchantId() {
        return merchantId;
    }
    /**
     * 设置：
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 获取：
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }
    /**
     * 设置：
     */
    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    /**
     * 获取：
     */
    public BigDecimal getOrderPrice() {
        return orderPrice;
    }
    /**
     * 设置：
     */
    public void setGoodPrice(BigDecimal goodPrice) {
        this.goodPrice = goodPrice;
    }

    /**
     * 获取：
     */
    public BigDecimal getGoodPrice() {
        return goodPrice;
    }
    /**
     * 设置：申请人ID
     */
    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    /**
     * 获取：申请人ID
     */
    public Integer getApplyId() {
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
     * 设置：默认是0：申请，1：审核通过，2：其他
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：默认是0：申请，1：审核通过，2：其他
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
    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    /**
     * 获取：审核操作人
     */
    public Integer getOperator() {
        return operator;
    }
}
