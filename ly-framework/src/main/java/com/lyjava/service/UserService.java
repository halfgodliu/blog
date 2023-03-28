package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.InsertUserDto;
import com.lyjava.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86198
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-02-23 16:32:59
*/
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult insertOrUpdateUser(InsertUserDto insertUserDto);

    ResponseResult deleteUser(Long id);

    ResponseResult getUpdateUser(Long id);
}
