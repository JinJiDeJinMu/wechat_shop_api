package com.chundengtai.base.task;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.util.Properties;

/**
 * @Description TODO
 * @Author hujinbiao
 * @Date 2020年3月24日 0024 上午 10:46:03
 * @Version 1.0
 **/
public class MybatisPluins implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
