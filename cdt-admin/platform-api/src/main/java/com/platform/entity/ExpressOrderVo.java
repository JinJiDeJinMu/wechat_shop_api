package com.platform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 实体
 * 表名 nideshop_express_order
 *
 * @author lipengjun
 * @date 2019-11-29 10:44:41
 */
@NoArgsConstructor
@Data
public class ExpressOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 
     */
    private Integer userId;
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
     * 姓名
     */
    private String name;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 配送时间
     */
    private String pickNumber;

    /**
     * 快递
     */
    private String expressNo;
    /**
     * 取件快递
     */
    private String getGoodsExpress;

    /**
     * 取件地址
     */
    private String getGoodsAddress;




}
