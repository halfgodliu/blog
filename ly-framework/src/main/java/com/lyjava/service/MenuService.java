package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86198
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2023-02-26 21:51:51
*/
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult<Menu> getMenuList(String status, String menuName);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenuById(Long menuId);

    ResponseResult treeSelect();

    ResponseResult roleMenuTreeSelect(Long id);
}
