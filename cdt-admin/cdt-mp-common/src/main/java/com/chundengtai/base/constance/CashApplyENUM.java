package com.chundengtai.base.constance;

public enum CashApplyENUM {


    NO_ADMIN_NOSHIRO("不是超级管理员，没有权限!"),
    NO_ADMIN_NO("管理员不能申请提现订单!"),
    MERCHANT_NOOPEN_CASH("商家未开通提现功能或者提现功能被冻结!"),
    ORDER_CASH_EXISTEN("申请提现订单已存在!"),
    ORDER_CASH_NOEXISTEN("申请提现订单不存在!"),
    ORDER_CASH_NOAPPLY("该订单状态未完成，不能提交申请!"),
    ORDER_CASH_APPLYSUCCESS("订单提现申请成功!"),
    ORDER_CASH_SUCCESS("订单提现审核通过!"),
    ORDER_CASH_REBACK("订单提现状态已通过或者驳回!"),
    ORDER_CASH_REJECT("订单提现审核驳回成功!"),
    ORDER_CASH_NOREJECT("提现订单不是审核状态，不能驳回!"),
    WHAT_PAY_ERROR("微信提现到商家账号失败!")
    ;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    CashApplyENUM(String msg) {
        this.msg = msg;
    }
}
