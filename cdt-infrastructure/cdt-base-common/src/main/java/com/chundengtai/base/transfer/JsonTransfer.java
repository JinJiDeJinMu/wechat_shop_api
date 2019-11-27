package com.chundengtai.base.transfer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonTransfer {

    public static <T> List<T> convertList(Object target, Class<T> clazz) {
        return JSONObject.parseArray(JSON.toJSONString(target), clazz);
    }

}
