package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Menu;
import com.lyjava.domain.vos.MenuVo;
import com.lyjava.domain.vos.RoleMenuTreeVo;
import com.lyjava.service.MenuService;
import com.lyjava.utils.BeanCopyUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 帮助菜单管理获取菜单列表
     * @param status
     * @param menuName
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取菜单列表")
    public ResponseResult<Menu> getMenuList(String status, String menuName){
        return menuService.getMenuList(status,menuName);
    }

    /**
     * 新增菜单的方法
     * @param menu
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜单")
    public ResponseResult insertMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 通过id获取菜单
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult<MenuVo> getMenuById(@PathVariable("id") Long id){
        //通过id查询菜单
        Menu menu = menuService.getById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        //封装返回
        return ResponseResult.okResult(menuVo);
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新菜单")
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    /**
     * 根据id删除菜单，存在子菜单不允许删除
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenuById(@PathVariable Long menuId){
        return menuService.deleteMenuById(menuId);
    }

    /**
     * 新增角色时需要获取菜单树列表
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        return menuService.treeSelect();
    }

    /**
     * 修改角色时，加载对应角色菜单列表树接口
     * @param id
     * @return
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult<RoleMenuTreeVo> roleMenuTreeSelect(@PathVariable("id") Long id){
        return menuService.roleMenuTreeSelect(id);
    }

}
