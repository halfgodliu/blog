package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Role;
import com.lyjava.domain.entity.RoleMenu;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.domain.vos.RoleVo;
import com.lyjava.service.RoleMenuService;
import com.lyjava.service.RoleService;
import com.lyjava.mapper.RoleMapper;
import com.lyjava.utils.BeanCopyUtils;
import com.lyjava.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author 86198
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2023-02-26 21:52:07
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //先判断用户id是否为1（管理员），
        if (SecurityUtils.isAdmin()){
            //如果是返回集合中只需要有admin
            List<String> roles = new ArrayList<>();
            roles.add("admin");
            return roles;
        }

        //当前用户id不是1则需要查询用户锁具有的角色
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    /**
     * 分页查询角色列表
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @param status
     * @return
     */
    @Override
    public ResponseResult<PageVo> getRoleListPage(Integer pageNum, Integer pageSize, String roleName, String status) {
        //查询条件
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.orderByAsc(Role::getRoleSort);

        //分页查询
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //封装返回
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        PageVo pageVo = new PageVo(roleVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 新增角色的方法
     * 更新角色也可以使用
     * @param roleVo
     * @return
     */
    @Override
    @Transactional
    public ResponseResult insertOrUpdateRole(RoleVo roleVo) {
        Role role = BeanCopyUtils.copyBean(roleVo, Role.class);
        //先判断传过来的数据有没有id
        if (roleVo.getId()==null){
            //没有有就进行角色表添加操作
            save(role);
        }else {
            //有有就进行角色表更新操作
            updateById(role);
        }


        //添加角色和菜单权限关联
        //先删除旧的角色和菜单关联关系
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuService.remove(queryWrapper);
        //存入新的数据
        List<RoleMenu> roleMenus = roleVo.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);

        return ResponseResult.okResult();
    }

    /**
     * 删除角色的方法
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteRole(Long id) {
        //删除角色
        removeById(id);
        //删除角色和菜单权限关联
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(queryWrapper);

        return ResponseResult.okResult();
    }

    /**
     * 新增用户前先获取角色信息
     * @return
     */
    @Override
    public ResponseResult listAllRole() {
        //查询状态是正常的角色
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roles = list(queryWrapper);

        return ResponseResult.okResult(roles);
    }
}




