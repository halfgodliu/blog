package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.entity.LoginUser;
import com.lyjava.domain.entity.User;
import com.lyjava.mapper.MenuMapper;
import com.lyjava.mapper.UserMapper;
import com.lyjava.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * springsecurity（BlogLoginServiceImpl.login()）内部默认会调用UserDetailsService的实现类来查询数据库
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    /**
     *
     * @param username 默认通过用户名来查询数据库，
     * @return 返回一个UserDetails实现类类型
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名在数据库中查询数据
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到了用户
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //判断用户是否是管理员，是管理员才要进行授权
        if (SystemConstants.ADMIN.equals(user.getType())){
            //查询当前用户所具有的权限（进行授权时需要）
            List<String> perms = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,perms);
        }
        //返回用户信息（不是管理员，不需要权限信息），需要自定义一个UserDetails的实现类来返回
        UserDetails loginUser = new LoginUser(user,null);
        return loginUser;
    }
}
