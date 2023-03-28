package com.lyjava.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.domain.entity.RoleMenu;
import com.lyjava.service.RoleMenuService;
import com.lyjava.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author 86198
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2023-03-01 16:18:19
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




