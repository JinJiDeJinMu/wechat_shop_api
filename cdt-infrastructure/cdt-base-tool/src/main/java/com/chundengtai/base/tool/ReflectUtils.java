package com.chundengtai.base.tool;

import com.platform.utils.Base64;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 反射工具类
 * @Author hujinbiao
 * @Date 2019年12月19日 0019 上午 10:31:30
 * @Version 1.0
 **/
public class ReflectUtils {

    /**
     * 根据类名反射获取属性然后加密
     *
     * @param object
     * @return
     */
    public static String getTokenByClassName(Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if (object == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String type = field.getGenericType().toString();
            String name = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            if (("Token").equals(name) || ("SerialVersionUID").equals(name) || ("Id").equals(name)) {
                continue;
            }
            Method method = object.getClass().getMethod("get" + name);
            Object val = method.invoke(object);
            String str = "";
            if (("class java.lang.String").equals(type)) {
                String value = (String) val;
                if (value != null) {
                    str = value;
                }
            }

            if (("class java.lang.Integer").equals(type)) {
                Integer value = (Integer) val;
                if (value != null) {
                    str = String.valueOf(value);
                }
            }

            if (("class java.math.BigDecimal").equals(type)) {
                BigDecimal value = (BigDecimal) val;
                if (value != null) {
                    str = value.toString();
                }
            }

            if (("class java.lang.Long").equals(type)) {
                Long value = (Long) val;
                if (value != null) {
                    str = String.valueOf(value);
                }
            }
            if (("class java.util.Date").equals(type)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date value = (Date) val;
                if (value != null) {
                    str = formatter.format(value);
                }
            }
            if (("calss java.lang.Boolean").equals(type)) {
                Boolean value = (Boolean) val;
                if (value != null) {
                    str = String.valueOf(value);
                }
            }
            if (("class java.lang.Double").equals(type)) {
                Double value = (Double) val;
                if (value != null) {
                    str = String.valueOf(value);
                }
            }

            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    /**
     * 对实体类加密获取token
     *
     * @param object
     * @return
     */
    public static String getToken(Object object) {
        String token = null;
        try {
            token = Base64.encode(ReflectUtils.getTokenByClassName(object));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return token;
    }
}
