package com.chundengtai.base.weixinapi;

public enum TrueOrFalseEnum {
    TRUE(1),
    FALSE(0);

    public Integer getCode() {
        return code;
    }

    private Integer code;
    private TrueOrFalseEnum(Integer code) {
        this.code = code;
    }

    //根据key获取枚举
    public static TrueOrFalseEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (TrueOrFalseEnum temp : TrueOrFalseEnum.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
