package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.User;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.exception.SystemException;
import com.lyjava.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    /**
     * 登录的方法
     * @param user 传过来的登录用户参数
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        //判断请求体参数中是否包含用户名，没有则抛出异常提示信息
        if (!StringUtils.hasText(user.getUserName())){
            //抛出自定义异常，并提示需要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    /**
     * 退出登录的方法
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
