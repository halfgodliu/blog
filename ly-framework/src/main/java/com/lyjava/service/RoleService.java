package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.domain.vos.RoleVo;

import java.util.List;

/**
* @author 86198
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2023-02-26 21:52:07
*/
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult<PageVo> getRoleListPage(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult insertOrUpdateRole(RoleVo roleVo);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();

}
