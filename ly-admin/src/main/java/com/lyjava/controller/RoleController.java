package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.RoleStatusDto;
import com.lyjava.domain.entity.Role;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.domain.vos.RoleVo;
import com.lyjava.service.RoleService;
import com.lyjava.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色列表（可能带有查询参数）
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> getRoleListPage(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.getRoleListPage(pageNum,pageSize,roleName,status);
    }

    /**
     * 改变角色状态
     * @param roleStatusDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){
        Role role = new Role();
        role.setId(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        roleService.updateById(role);
        return ResponseResult.okResult();
    }

    /**
     * 新增角色
     * @return
     */
    @PostMapping
    public ResponseResult insertRole(@RequestBody RoleVo roleVo){
        return roleService.insertOrUpdateRole(roleVo);
    }

    @GetMapping("/{id}")
    public ResponseResult<RoleVo> getById(@PathVariable Long id){
        Role role = roleService.getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    /**
     * 执行更新角色操作的接口
     * @param roleVo
     * @return
     */
    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleVo roleVo){
        return roleService.insertOrUpdateRole(roleVo);
    }

    /**
     * 删除角色的接口
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long id){
        return roleService.deleteRole(id);
    }

    /**
     * 新增用户前先获取角色信息
     * @return
     */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
