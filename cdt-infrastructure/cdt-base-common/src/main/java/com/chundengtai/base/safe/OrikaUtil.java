package com.chundengtai.base.safe;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
 
import java.util.List;
 
/**
 *
 * Orika是一个简单、快速的JavaBean拷贝框架，Orika使用字节代码生成来创建具有最小开销的快速映射器。
 *
 */
public class OrikaUtil {

    private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public static <A, B> ClassMapBuilder<A, B> classMap(Class<A> source, Class<B> target) {
        return mapperFactory.classMap(source, target);
    }

    public static <T> T convert(Object source, Class<T> target) {
        return mapperFactory.getMapperFacade().map(source, target);
    }

    public static <S, D> List<D> convertList(Iterable<S> source, Class<D> target) {
        return mapperFactory.getMapperFacade().mapAsList(source, target);
    }
}