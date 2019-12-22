package com.platform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首次登录对象
 */
@Data
@NoArgsConstructor
public class LoginInfo {
    private String code;
    private String avatarUrl;
    private int gender;
    private String nickName;
    private String language;
    private String city;
    private String province;
    private String country;
	private int promoterId;
	private Long merchantId;	//商户id
	private String referrerId;
}
