package com.lyjava.handler.exception;

import com.lyjava.domain.ResponseResult;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
//@ControllerAdvice：全局接口异常处理,与@ExceptionHandler(Exception.class)一起使用可以全局捕获指定异常
//@ResponseBody：表示方法的返回值是数据
//RestControllerAdvice：相当于@ControllerAdvice和@ResponseBody的复合注解
@RestControllerAdvice
@Slf4j//使用日志
public class GlobalExceptionHandler {

    //捕获异常，抛出了SystemException异常会由这里处理
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //日志打印异常信息
        log.error("出现了异常！{}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    //其它Exception异常由这里处理（SystemException异常除外）
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //日志打印异常信息
        log.error("出现了异常！{}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}
