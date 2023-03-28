package com.lyjava.domain.vos;

import com.lyjava.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeVo {

    /**
     * 所有菜单
     */
    private List<Menu> menus;
    /**
     * 角色所关联的菜单权限id列表
     */
    private List<Long> checkedKeys;
}
