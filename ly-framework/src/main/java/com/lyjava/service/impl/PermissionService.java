package com.lyjava.service.impl;

import com.lyjava.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 进行授权（判断当前用户是否具有权限）
 */
@Service("ps")//使用注解判断是否具有权限@PreAuthorize("@ps.hasPermission('content:category:export')")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission（权限）
     * @param permission 需要的权限
     * @return
     */
    public boolean hasPermission(String permission){
        //如果当前用户id是1（超级管理员），则直接返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }
        //否则获取当前用户所具有的权限，并判断是否具有所需要的权限
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
