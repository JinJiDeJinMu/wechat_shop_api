package com.chundengtai.base.exception;

/**
 * 错误码接口
 *
 * @author
 */
public interface ErrorCode {

    /**
     * 获取错误码
     *
     * @return
     */
    String getCode();

    /**
     * 获取错误信息
     *
     * @return
     */
    String getDescription();
}
