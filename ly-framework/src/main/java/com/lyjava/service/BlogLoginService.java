package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
