package com.chundengtai.base.serializer;

/**
 * @version 1.0.0
 * @description FastJsonWraper包装类
 */
public class FastJsonWraper<T> {
    private T value;

    public FastJsonWraper() {
    }

    public FastJsonWraper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
