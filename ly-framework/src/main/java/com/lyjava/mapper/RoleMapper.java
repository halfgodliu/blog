package com.lyjava.mapper;

import com.lyjava.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86198
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2023-02-26 21:52:07
* @Entity com.lyjava.domain.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}




