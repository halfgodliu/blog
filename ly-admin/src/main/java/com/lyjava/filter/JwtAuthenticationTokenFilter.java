package com.lyjava.filter;

import com.alibaba.fastjson.JSON;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.LoginUser;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.utils.JwtUtil;
import com.lyjava.utils.RedisCache;
import com.lyjava.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 自定义token校验过滤器，继承OncePerRequestFilter
 */
@Component//注入容器中
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        //如果没有token说明该接口不需要登录，直接放行
        if (!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }
        //解析token获取userId，
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //执行到这里说明解析失败，token非法或超时
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //响应给前端，并说明需要重新登陆
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject(SystemConstants.REDIS_ADMINLOGIN_PRE+userId);
        if (Objects.isNull(loginUser)){//如果获取不到，说明登录过期
            //登录过期，提示重新登陆
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(request,response);
    }
}
