package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.InsertUserDto;
import com.lyjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询角色列表
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param phonenumber
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getList(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.getList(pageNum,pageSize,userName,phonenumber,status);
    }

    /**
     * 新增用户的接口
     * @param insertUserDto
     * @return
     */
    @PostMapping
    public ResponseResult insertUser(@RequestBody InsertUserDto insertUserDto){
        return userService.insertOrUpdateUser(insertUserDto);
    }

    /**
     * 删除用户的接口
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        return userService.deleteUser(id);
    }

    /**
     * 修改用户前先获取用户数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult updateGetUser(@PathVariable Long id){
        return userService.getUpdateUser(id);
    }

    /**
     * 修改用户的接口
     * @param insertUserDto
     * @return
     */
    @PutMapping
    public ResponseResult updateUser(@RequestBody InsertUserDto insertUserDto){
        return userService.insertOrUpdateUser(insertUserDto);
    }
}
