package com.lyjava.exception;

import com.lyjava.enums.AppHttpCodeEnum;

/**
 * 自定义异常
 */
public class SystemException extends RuntimeException{
    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 本自定义异常的构造方法
     * @param httpCodeEnum 抛出本异常时要传入一个AppHttpCodeEnum枚举
     */
    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
