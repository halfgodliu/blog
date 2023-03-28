package com.lyjava.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.domain.entity.UserRole;
import com.lyjava.service.UserRoleService;
import com.lyjava.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 86198
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2023-03-01 20:37:05
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




