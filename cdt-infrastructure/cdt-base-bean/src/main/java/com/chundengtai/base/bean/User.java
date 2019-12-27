package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 *
 * </p>
 *
 * @author Royal
 * @since 2019-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("nideshop_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private Integer gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthday;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    private String lastLoginIp;

    private Integer userLevelId;

    private String nickname;

    private String mobile;

    private String registerIp;

    private String avatar;

    private String weixinOpenid;

    /**
     * 可用抽奖金额
     */
    private BigDecimal getMoney;

    /**
     * 抽奖次数
     */
    private Integer getNum;

    private Integer brandId;

    /**
     * 推荐人ID
     */
    private Integer parentUserId;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 推广人id
     */
    private Integer promoterId;

    /**
     * 推广人姓名
     */
    private String promoterName;

    /**
     * 是否实名认证 1：否 2：是
     */
    private String isReal;

    /**
     * 是否推荐购买返现 0没有、1已返现
     */
    private Integer isReturnCash;

    /**
     * 首次购买金额
     */
    private BigDecimal firstBuyMoney;

    /**
     * 二维码路径
     */
    private String qrCode;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 总佣金
     */
    private BigDecimal totalBrokerage;

    /**
     * 可提现金额
     */
    private BigDecimal validBrokerage;

    /**
     * 已提现金额
     */
    private BigDecimal withdrawCash;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 是否分销商
     */
    private Integer isDistribut;

    /**
     * 第一个上级
     */
    private Integer firstLeader;

    /**
     * 第二个上级
     */
    private Integer secondLeader;


}
