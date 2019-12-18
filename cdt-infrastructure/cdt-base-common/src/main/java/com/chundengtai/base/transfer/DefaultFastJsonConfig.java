package com.chundengtai.base.transfer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import java.math.BigInteger;

/**
 * fastJson序列化配置.
 *
 * @author kexue
 */
public class DefaultFastJsonConfig extends FastJsonConfig {

    public DefaultFastJsonConfig() {
        super();
        /**
         * 处理长整形型丢失精度的问题.
         */
        getSerializeConfig().put(Long.class, ToStringSerializer.instance);
        getSerializeConfig().put(Long.TYPE, ToStringSerializer.instance);
        getSerializeConfig().put(BigInteger.class, ToStringSerializer.instance);

        //设置特性
        /**
         * <!-- 输出空置字段 -->
         <value>WriteMapNullValue</value>
         <!-- 浏览器和设备兼容 -->
         <value>BrowserCompatible</value>
         <!-- list字段如果为null，输出为[]，而不是null -->
         <value>WriteNullListAsEmpty</value>
         <!-- 字符类型字段如果为null，输出为""，而不是null -->
         <value>WriteNullStringAsEmpty</value>
         <!-- 日期格式化 -->
         <value>WriteDateUseDateFormat</value>
         <value>WriteEnumUsingToString</value>
         */
        setSerializerFeatures(new SerializerFeature[]{
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteEnumUsingToString,});

    }
}