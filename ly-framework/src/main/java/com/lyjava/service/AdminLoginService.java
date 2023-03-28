package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.User;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
