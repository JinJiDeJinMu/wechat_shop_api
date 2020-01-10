package com.chundengtai.base.util;

public enum BannerType {
    ACTIVITY("活动的", 0), HOT("热门", 1), BLANK("白色", 3), YELLO("黄色", 4);
    // 成员变量
    private String name;

    public int getCode() {
        return code;
    }

    private int code;

    // 构造方法
    private BannerType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    // 普通方法
    public static String getName(int index) {
        for (BannerType c : BannerType.values()) {
            if (c.getCode() == index) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

}