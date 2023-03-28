package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.LoginUser;
import com.lyjava.domain.entity.Menu;
import com.lyjava.domain.entity.User;
import com.lyjava.domain.vos.AdminUserInfoVo;
import com.lyjava.domain.vos.RoutersVo;
import com.lyjava.domain.vos.UserInfoVo;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.exception.SystemException;
import com.lyjava.service.AdminLoginService;
import com.lyjava.service.BlogLoginService;
import com.lyjava.service.MenuService;
import com.lyjava.service.RoleService;
import com.lyjava.utils.BeanCopyUtils;
import com.lyjava.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    /**
     * 登录的方法
     * @param user 传过来的登录用户参数
     * @return
     */
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        //判断请求体参数中是否包含用户名，没有则抛出异常提示信息
        if (!StringUtils.hasText(user.getUserName())){
            //抛出自定义异常，并提示需要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    /**
     * 获取登录用户各种信息的
     * @return
     */
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据id查询角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取当前登录用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roles,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    /**
     * 获取路由数据的，
     * @return
     */
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询路由数据，该数据应该是要有层级关系（tree形式）的，不能直接就是一个list集合
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);

        //封装返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    /**
     * 退出登录的方法
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }
}
