package com.platform.controller;

import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.ShiroUtils;
import com.platform.constance.ShopShow;

import java.util.Map;

public class BaseController {


    public Query getqCurrentQuery(Map<String, Object> params) {
        Query query = new Query(params);
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            query.put("merchant_id", ShiroUtils.getUserEntity().getMerchantId());
        }
        return query;
    }

    public Map<String, Object> getCurrentUserMap(Map<String, Object> params) {
        if (ShiroUtils.getUserEntity().getMerchantId() != ShopShow.ADMINISTRATOR.getCode()) {
            params.put("merchant_id", ShiroUtils.getUserEntity().getMerchantId());
        }

        return params;
    }
}
