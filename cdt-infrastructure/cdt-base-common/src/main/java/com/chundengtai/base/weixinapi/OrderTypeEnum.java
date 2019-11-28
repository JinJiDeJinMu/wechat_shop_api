package com.chundengtai.base.weixinapi;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :
 * 类 名 称    :订单状态枚举
 * 功能描述    :
 * 业务描述    :
 * 作 者 名    :@Author Royal
 * 开发日期    :${Date}-${Time}
 * Created     :IntelliJ IDEA
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
public enum OrderTypeEnum {
    WAIT_PAY(0, "待付款"),
    CANCEL_ORDER(101, "订单已取消"),
    DELETE_ORDER(102, "订单已删除"),
    CLOSED_ORDER(103, "订单已关闭"),
    PAYED_ORDER(201, "订单已付款"),


    WRITE_OFF_PAY(202, "未核销"),
    WRITE_OFF_PAYED(203, "已核销"),


    EXPRESS_NO_GET(204, "未代取"),
    EXPRESS_GETED(205, "已代取"),

    SHIPPED_ORDER(300, "订单已发货"),
    CONFIRM_GOODS(301, "用户确认收货"),
    REFUND_ORDER(401, "订单已退款"),
    COMPLETED_ORDER(402, "订单完成"),

    APPLY_REFUND_GOODS(501, "买家申请退货"),
    GOOD_SENDING(502, "退货寄回中"),
    STORM_GET_GOODS(503, "仓库已收退货"),
    STORM_REJECT_GOODS(504, "仓库拒绝退货");


    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }

    private String desc;
    private Integer code;

    private OrderTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderTypeEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (OrderTypeEnum temp : OrderTypeEnum.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
