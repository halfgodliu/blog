package com.lyjava.controller;

import com.lyjava.annotation.SystemLog;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.User;
import com.lyjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询个人信息
     * @return
     */
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    /**
     * 更新个人信息
     * @param user 个人信息
     * @return
     */
    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新个人信息")//用于添加通知来打印日志
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    /**
     * 注册的方法
     * @param user 注册的用户信息
     * @return
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
