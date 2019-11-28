package com.chundengtai.base.weixinapi;
/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :
 * 类 名 称    :商品类型枚举
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
public enum GoodsTypeEnum {
    ORDINARY_GOODS(1,"普通商品"),
    SECONDS_KILL(2,"秒杀"),
    GROUP_GOODS(3, "团购"),
    EXPRESS_GET(4, "快递代取"),
    WRITEOFF_ORDER(5, "核销订单");

    private Integer code;
    private String desc;
    GoodsTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static GoodsTypeEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (GoodsTypeEnum temp : GoodsTypeEnum.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
