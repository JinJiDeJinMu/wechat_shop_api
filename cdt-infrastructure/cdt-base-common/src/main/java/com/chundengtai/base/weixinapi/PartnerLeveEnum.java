package com.chundengtai.base.weixinapi;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :有机屋
 * 类 名 称    :合伙人等级枚举类
 * 功能描述    :
 * 业务描述    :
 * 作 者 名    :@Author amtf
 * 开发日期    :2020/1/2 下午2:23
 * Created    :IntelliJ IDEA
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
public enum PartnerLeveEnum {
    ONE(1, "一级合伙人"),
    TWO(2, "二级合伙人"),
    THREE(3, "三级合伙人");

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }

    private String desc;
    private Integer code;

    private PartnerLeveEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PartnerLeveEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (PartnerLeveEnum temp : PartnerLeveEnum.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
