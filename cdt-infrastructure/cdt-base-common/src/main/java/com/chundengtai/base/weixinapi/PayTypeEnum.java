package com.chundengtai.base.weixinapi;

public enum PayTypeEnum {

    NOPAY(0, "未支付"),
    PAYING(1, "支付中"),
    PAYED(2, "已支付"),
    REFUND(4, "已退款"),
    STORM_GET_GOODS_NEED_REFUND(5, "仓库收到退货待退款"),
    STORM_GET_GOODS_REFUNDED(6, "仓库收到退货退款完成");

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }

    private String desc;
    private Integer code;

    private PayTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    //根据key获取枚举
    public static PayTypeEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (PayTypeEnum temp : PayTypeEnum.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
