package com.chundengtai.base.constant;

public class CacheConstant {

    /**
     * 商城业务缓存
     */
    public final static String SYSTEM = "SYSTEM";

    /**
     * 系统AccessToken
     */
    public final static String SYSTEM_ACCESS_TOKEN = "SYSTEM_ACCESS_TOKEN";

    /**
     * 系统缓存
     */
    private final static String SYS_CACHE_NAME = "sysCache";

    /**
     * 身份验证
     */
    public final static String SYS_ID_CARD_CHECL = "idcardCheck";

    /**
     * 商城业务缓存
     */
    public static final String SHOP_CACHE_NAME = "shopCache";

    public static final String GOODS_NAME = "goods";

    public static final String SHOP_GOODS_CACHE = SHOP_CACHE_NAME + ":" + GOODS_NAME + ":";

    public static final String EXPRESS_GOODS_CACHE =  "expressGoodsCache";


    public static final String ORDER_HEXIAO_CACHE = "checkHexiao:";

}
