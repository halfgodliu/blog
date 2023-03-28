package com.lyjava.mapper;

import com.lyjava.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 86198
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2023-02-26 21:51:51
* @Entity com.lyjava.domain.entity.Menu
*/
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> treeMenuAll();
}




