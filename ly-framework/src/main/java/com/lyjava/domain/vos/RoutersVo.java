package com.lyjava.domain.vos;

import com.lyjava.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回给前端的路由数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {
    private List<Menu> menus;
}
