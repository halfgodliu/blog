package com.lyjava.handler.security;

import com.alibaba.fastjson.JSON;
import com.lyjava.domain.ResponseResult;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    /**
     * 认证失败时内部会执行该方法
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //打印异常信息
        authException.printStackTrace();
        ResponseResult result = null;
        //BadCredentialsException：用户名或密码错误抛的这个异常
        //InsufficientAuthenticationException：没有携带token会抛这个异常
        //进行判断是什么导致的异常
        if (authException instanceof BadCredentialsException){
            //说明用户名或密码错误
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),
                    authException.getMessage());
        }else if (authException instanceof InsufficientAuthenticationException){
            //说明没有携带token
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {//发生其它异常时
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");
        }

        //响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
