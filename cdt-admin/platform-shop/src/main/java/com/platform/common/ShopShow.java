package com.platform.common;

public enum ShopShow {
    ADMINISTRATOR("活动的", 1), SHOPBOSS("店家", 0);
    // 成员变量
    private String name;

    public int getCode() {
        return code;
    }

    private int code;

    // 构造方法
    private ShopShow(String name, int code) {
        this.name = name;
        this.code = code;
    }

    // 普通方法
    public static String getName(int index) {
        for (ShopShow c : ShopShow.values()) {
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
