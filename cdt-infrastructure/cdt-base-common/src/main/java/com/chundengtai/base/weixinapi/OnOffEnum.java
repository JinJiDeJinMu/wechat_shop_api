package com.chundengtai.base.weixinapi;

public enum OnOffEnum {
    ON(1),
    OFF(0);

    public Integer getCode() {
        return code;
    }

    private Integer code;

    private OnOffEnum(Integer code) {
        this.code = code;
    }

    //根据key获取枚举
    public static OnOffEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (OnOffEnum temp : OnOffEnum.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
