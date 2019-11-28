package com.chundengtai.base.weixinapi;

public enum OrderType {
    NORMAL(0, "待付款"),
    EXPRESS(1, "快递代取"),
    HEXIAO(2, "核销订单");

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }

    private String desc;
    private Integer code;

    private OrderType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderType getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (OrderType temp : OrderType.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
