package com.lyjava.domain.vos;

import com.lyjava.domain.entity.Role;
import com.lyjava.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用来封装修改用户前查询的用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUpdateUserVo {

    /**
     * 用户信息
     */
    private User user;
    /**
     * 用户所关联的角色id列表
     */
    private List<Long> roleIds;
    /**
     * 所有角色的列表
     */
    private List<Role> roles;
}
