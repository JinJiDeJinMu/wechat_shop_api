package com.chundengtai.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 客户实体
 */
@NoArgsConstructor
@Data
public class CustomerEntity implements Serializable {
    //主键
    private Integer id;
    //会员姓名
    private String uname;
    //客户性别 0:未知性别 1：男 2：女
    private String gender;
    //出生日期
    private Date birthday;
    //手机号码
    private String mobile;
    //邮寄地址用户
    private int addressUserId;
    //维护状态 0：未维护 1:已维护
    private String upkeepState;
    //客户属性 0:默认用户 1：准客户
    private String customerState;
    //备注
    private String remarks;

    //创建时间
    private Date ctime;
    //创建人id
    private Integer uid;
    //工作
    private String job;
    private String end_date;
    private String realName;
    //手机号码
    private String mobile2;
    //收货地址
    private AddressEntity addressEntity;
    //客户维护历史
    private List<UpkeepEntity> upkeepEntity;
}
