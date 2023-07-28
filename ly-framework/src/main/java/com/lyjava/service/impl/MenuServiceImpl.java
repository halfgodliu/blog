package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Menu;
import com.lyjava.domain.entity.RoleMenu;
import com.lyjava.domain.vos.RoleMenuTreeVo;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.service.MenuService;
import com.lyjava.mapper.MenuMapper;
import com.lyjava.service.RoleMenuService;
import com.lyjava.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86198
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2023-02-26 21:51:51
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 根据用户id查询权限信息
     * @param userId 用户id
     * @return
     */
    @Override
    public List<String> selectPermsByUserId(Long userId) {
        //先判断用户id是否为1（管理员）
        if (SecurityUtils.isAdmin()){
            //查询所有menuType（菜单类型）为M或者F的，状态为正常的菜单
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            //转换成List<String>
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        //用户id不为1，则查询用户所具有的权限并返回
        return getBaseMapper().selectPermsByUserId(userId);
    }

    /**
     * 根据userId查询路由数据并以tree的形式（层级关系）返回
     * @param userId
     * @return
     */
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        //先判断userId是否为1（管理员）
        if (SecurityUtils.isAdmin()){
            //是，则直接查询所有菜单类型为C或者M的，状态为正常的，未被删除的权限
            menus = getBaseMapper().selectAllRouterMenu();
        }else {
            //否，则根据userId查询对应用户所具有的
            menus = getBaseMapper().selectRouterMenuTreeByUserId(userId);
        }

        //将list集合转换成tree的形式，并返回
        List<Menu> menuTree = buildMenuTree(menus,0L);
        return menuTree;
    }

    /**
     * 获取菜单列表
     * @param status
     * @param menuName
     * @return
     */
    @Override
    public ResponseResult<Menu> getMenuList(String status, String menuName) {
        //查询菜单列表
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        List<Menu> menus = list(queryWrapper);
        //封装返回
        return ResponseResult.okResult(menus);
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @Override
    public ResponseResult updateMenu(Menu menu) {
        //父菜单不能设置为当前菜单
        if (menu.getId().equals(menu.getParentId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),
                    "修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        //父菜单不是当前菜单则进行更新
        updateById(menu);
        return ResponseResult.okResult();
    }

    /**
     * 根据id删除菜单，存在子菜单不允许删除
     * @param menuId
     * @return
     */
    @Override
    public ResponseResult deleteMenuById(Long menuId) {
        //先查询有没有子菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> childrenMenus = list(queryWrapper);

        if (!childrenMenus.isEmpty()){
            //如果存在子菜单则不允许删除
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),
                    "存在子菜单不允许删除");
        }
        //不存在子菜单
        removeById(menuId);
        return ResponseResult.okResult();
    }

    /**
     * 新增角色时需要获取菜单树列表
     * @return
     */
    @Override
    public ResponseResult treeSelect() {
        //先查询所有菜单集合
        List<Menu> menus = getBaseMapper().treeMenuAll();

        //将list集合转换成tree的形式，并返回
        List<Menu> menuTree = buildMenuTree(menus,0L);
        return ResponseResult.okResult(menuTree);
    }

    /**
     * 修改角色时，加载对应角色菜单列表树接口
     * @param id
     * @return
     */
    @Override
    public ResponseResult<RoleMenuTreeVo> roleMenuTreeSelect(Long id) {
        //先查询所有角色菜单
        List<Menu> menus = getBaseMapper().treeMenuAll();
        //构建成树的形式
        List<Menu> menuTree = buildMenuTree(menus, 0L);

        //角色所关联的菜单权限id列表
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> roleMenus = roleMenuService.list(queryWrapper);
        List<Long> checkedKeys = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());

        //封装返回
        RoleMenuTreeVo roleMenuTreeVo = new RoleMenuTreeVo(menuTree,checkedKeys);
        return ResponseResult.okResult(roleMenuTreeVo);
    }

    /**
     * 将menu集合转换成tree的形式（具有父子关系，层级关系）
     * @param menus
     * @return
     */
    private List<Menu> buildMenuTree(List<Menu> menus,Long parentId) {
        List<Menu> menuList = menus.stream()
                //先过滤出来所有父菜单
                .filter(menu -> menu.getParentId().equals(parentId))
                //调用自定义的方法把子菜单存入父菜单的children属性
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuList;
    }

    /**
     * 获取子菜单
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        //找出菜单的属性
        List<Menu> childrenList = menus.stream()
                .filter(menuChildren -> menu.getId().equals(menuChildren.getParentId()))
                .map(m -> m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        //将子菜单存入父菜单的children属性并返回
        return childrenList;
    }
}




