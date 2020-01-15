package com.chundengtai.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 类描述：分销用户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MlsUserEntity2 implements Serializable{
	private Long mlsUserId;
	private String userName;
	private String nickname;
	private String loginPassword;
	private String deviceId;
	private String userTel;
	private String profilePhoto;
	private Integer allSales;
	private Integer todaySales;
	private Integer allProfit;
	private Integer getProfit;
	private Integer fatherUserId;
	private Integer sonUserSum;
	private Long userId;
	private Timestamp ctime;
	//是否显示其他商户 0:不显示，1:显示
	private int allShow;
	//一级分佣
    private double fx1;
    //二级分佣
    private double fx2;
    //本人分佣
    private double fx;
    //平台分佣
    private double pfx;
    //父ID
    private Long fid;
    //分销用户根节点
    private Long rootId;
	//商户id
	private Long merchantId;

}