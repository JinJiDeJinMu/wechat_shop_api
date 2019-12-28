package com.chundengtai.base.weixinapi;

public enum DistributionTypeEnum {
    SHARE(0, "分销"),
    PARTNER(1, "合伙人");

    private Integer code;
    private String desc;

    DistributionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DistributionTypeEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (DistributionTypeEnum temp : DistributionTypeEnum.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
