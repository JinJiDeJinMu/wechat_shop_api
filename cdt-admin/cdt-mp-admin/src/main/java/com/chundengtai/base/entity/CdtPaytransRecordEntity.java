package com.chundengtai.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统流水日志实体
 */
@Data
@NoArgsConstructor
public class CdtPaytransRecordEntity implements Serializable {
    /**
     *
     */
    private Long id;
    /**
     *
     */
    private Integer rid;
    /**
     * 名称
     */
    private String name;
    /**
     * 商品备注
     */
    private String memo;
    /**
     * 创建者id
     */
    private Integer creator;
    /**
     * 创建者名称
     */
    private String creatorName;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 数据等级
     */
    private Integer dataLevel;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单id
     */
    private Integer payOrderId;
    /**
     * 支付商户id
     */
    private String mchId;
    /**
     * 支付订单号
     */
    private String mchOrderNo;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 总金额
     */
    private BigDecimal amount;
    /**
     * 交易金额
     */
    private BigDecimal tradePrice;
    /**
     * 支付状态
     */
    private Integer payState;
    /**
     * 支付类型
     */
    private Integer payType;
    /**
     * app_id
     */
    private String appId;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 请求文本
     */
    private String reqText;
    /**
     * 返回文本
     */
    private String resText;

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：
     */
    public void setRid(Integer rid) {
        this.rid = rid;
    }

    /**
     * 获取：
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * 设置：名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：商品备注
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 获取：商品备注
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置：创建者id
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取：创建者id
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置：创建者名称
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * 获取：创建者名称
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * 设置：创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置：数据等级
     */
    public void setDataLevel(Integer dataLevel) {
        this.dataLevel = dataLevel;
    }

    /**
     * 获取：数据等级
     */
    public Integer getDataLevel() {
        return dataLevel;
    }

    /**
     * 设置：订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取：订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置：订单id
     */
    public void setPayOrderId(Integer payOrderId) {
        this.payOrderId = payOrderId;
    }

    /**
     * 获取：订单id
     */
    public Integer getPayOrderId() {
        return payOrderId;
    }

    /**
     * 设置：支付商户id
     */
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    /**
     * 获取：支付商户id
     */
    public String getMchId() {
        return mchId;
    }

    /**
     * 设置：支付订单号
     */
    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    /**
     * 获取：支付订单号
     */
    public String getMchOrderNo() {
        return mchOrderNo;
    }

    /**
     * 设置：渠道id
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * 获取：渠道id
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * 设置：总金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取：总金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置：交易金额
     */
    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    /**
     * 获取：交易金额
     */
    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    /**
     * 设置：支付状态
     */
    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    /**
     * 获取：支付状态
     */
    public Integer getPayState() {
        return payState;
    }

    /**
     * 设置：支付类型
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * 获取：支付类型
     */
    public Integer getPayType() {
        return payType;
    }

    /**
     * 设置：app_id
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取：app_id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置：更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置：请求文本
     */
    public void setReqText(String reqText) {
        this.reqText = reqText;
    }

    /**
     * 获取：请求文本
     */
    public String getReqText() {
        return reqText;
    }

    /**
     * 设置：返回文本
     */
    public void setResText(String resText) {
        this.resText = resText;
    }

    /**
     * 获取：返回文本
     */
    public String getResText() {
        return resText;
    }
}
