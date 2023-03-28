package com.lyjava.service.impl;

import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.LoginUser;
import com.lyjava.domain.entity.User;
import com.lyjava.domain.vos.BlogUserLoginVo;
import com.lyjava.domain.vos.UserInfoVo;
import com.lyjava.service.BlogLoginService;
import com.lyjava.utils.BeanCopyUtils;
import com.lyjava.utils.JwtUtil;
import com.lyjava.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    /**
     * 默认情况下是没有的，所以要在配置类中将它注入到容器中，
     * 该配置类前台和后台可能不通用，所以不要写到公共模块中
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录的方法
     * @param user 传过来的登录用户参数
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        //通过AuthenticationManager的authenticate()方法来进行用户认证
        //该方法要传入一个Authentication接口的实现类对象
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //内部会调用UserDetailsService的实现类来查询数据库
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //判断认证是否通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        //获取userid，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        //把用户信息存入redis，过期时间为24小时
        redisCache.setCacheObject(SystemConstants.REDIS_BLOGLOGIN_PRE+userId,loginUser,24, TimeUnit.HOURS);

        //把user对象转换成UserInfoVo（UserInfo）对象
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(),UserInfoVo.class);
        //把token和userInfo封装，返回
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    /**
     * 退出登录的方法
     * @return
     */
    @Override
    public ResponseResult logout() {
        //从SecurityContextHolder中获取loginUser信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
