package com.lyjava.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用来接收新增和修改用户时传过来的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertUserDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phonenumber;

    private String sex;

    private String email;
    /**
     * 关联的角色id
     */
    private List<Long> roleIds;
}
