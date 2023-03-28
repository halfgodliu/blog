package com.lyjava.utils;

import com.lyjava.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 工具类，获取loginUser，userId，Authentication等
 */
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

    /**
    * 获取用户id
    */
    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}