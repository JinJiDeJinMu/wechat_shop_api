package com.chundengtai.base.weixinapi;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :快递状态
 * 类 名 称    :
 * 功能描述    :
 * 业务描述    :
 * 作 者 名    :@Author amtf
 * 开发日期    :2018/10/24 0024-下午 18:01
 * Created     :IntelliJ IDEA
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
public enum ShippingTypeEnum {
    NOSENDGOODS(0, "未发货"),
    SENDGOODS(1, "已发货"),
    GETEDGOODS(2, "已收货"),
    REFUNDGOODS(4, "退货");

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }

    private String desc;
    private Integer code;

    private ShippingTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    //根据key获取枚举
    public static ShippingTypeEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (ShippingTypeEnum temp : ShippingTypeEnum.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
