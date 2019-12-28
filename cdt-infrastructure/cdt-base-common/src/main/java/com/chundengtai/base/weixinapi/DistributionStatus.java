package com.chundengtai.base.weixinapi;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :
 * 类 名 称    :分销系统状态枚举
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
public enum DistributionStatus {

    NOT_SERVEN_ORDER(398, "未满七天"),
    REFUND_ORDER(399, "已退订"),
    NON_COMPLETE_ORDER(400, "未完成"),
    COMPLETED_ORDER(402, "已完成"),
    COMPLETED_GETGOLD(403, "已提现");

    private Integer code;
    private String desc;

    DistributionStatus(Integer code, String desc) {
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

    public static DistributionStatus getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (DistributionStatus temp : DistributionStatus.values()) {
            if (temp.getCode().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
