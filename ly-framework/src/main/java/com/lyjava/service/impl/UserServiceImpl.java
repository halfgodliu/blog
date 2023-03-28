package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.InsertUserDto;
import com.lyjava.domain.entity.Role;
import com.lyjava.domain.entity.User;
import com.lyjava.domain.entity.UserRole;
import com.lyjava.domain.vos.GetUpdateUserVo;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.domain.vos.UserInfoVo;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.exception.SystemException;
import com.lyjava.service.RoleService;
import com.lyjava.service.UserRoleService;
import com.lyjava.service.UserService;
import com.lyjava.mapper.UserMapper;
import com.lyjava.utils.BeanCopyUtils;
import com.lyjava.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86198
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-02-23 16:32:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    /**
     * 获取个人信息
     * @return
     */
    @Override
    public ResponseResult userInfo() {
        //获取userId
        Long userId = SecurityUtils.getUserId();
        //通过userId查询
        User user = getById(userId);
        //封装Vo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    /**
     * 更新个人信息
     * @param user 个人信息
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    /**
     * 注册的方法
     * @param user 注册的用户信息
     * @return
     */
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断，用户名、密码、昵称、邮箱不能为空
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        //查询用户名和昵称是否存在
        if (userNameExit(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExit(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        //对密码进行加密，并设置到user中
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        //存入数据库
        save(user);

        return ResponseResult.okResult();
    }

    /**
     * 查询角色列表
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param phonenumber
     * @param status
     * @return
     */
    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        //查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        //分页查询
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //封装返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 新增或修改用户的方法（通过有无id判断是新增还是修改）
     * @param insertUserDto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult insertOrUpdateUser(InsertUserDto insertUserDto) {

        //转换格式
        User user = BeanCopyUtils.copyBean(insertUserDto, User.class);
        //先判断id有没有值
        if (user.getId()==null){
            //对数据进行非空判断，用户名不能为空
            if (!StringUtils.hasText(insertUserDto.getUserName())){
                throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
            }

            //查询用户名和昵称是否存在
            if (userNameExit(insertUserDto.getUserName())){
                throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
            }
            if (phonenumberExit(insertUserDto.getPhonenumber())){
                throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
            }
            if (emailExit(insertUserDto.getEmail())){
                throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
            }

            //没有值就进行添加操作
            //对密码进行加密，并设置到user中
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            //保存用户数据
            save(user);
        }else {
            //有值则进行更新操作
            updateById(user);
        }

        //保存用户和角色关联数据
        //先删除旧数据
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(queryWrapper);
        //存入新的数据
        List<UserRole> userRoles = insertUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);

        return ResponseResult.okResult();
    }

    /**
     * 删除用户的接口
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResponseResult deleteUser(Long id) {
        //删除用户表中的数据
        removeById(id);
        //删除用户和角色关联表中的数据
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        userRoleService.remove(queryWrapper);

        return ResponseResult.okResult();
    }

    /**
     * 修改用户前先获取用户数据
     * @param id
     * @return
     */
    @Override
    public ResponseResult getUpdateUser(Long id) {
        //获取用户信息
        User user = getById(id);
        //获取所有角色列表
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roles = roleService.list(wrapper);
        //获取用户所关联的角色id列表
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleService.list(queryWrapper);
        List<Long> roleIds = userRoles.stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());

        //将数据封装返回
        GetUpdateUserVo getUpdateUserVo = new GetUpdateUserVo(user,roleIds,roles);
        return ResponseResult.okResult(getUpdateUserVo);
    }

    /**
     * 查询昵称是否已存在
     * @param nickName
     * @return
     */
    private boolean nickNameExit(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);

        //根据查询到的数据条数判断昵称是否已存在
        return count(queryWrapper)>0;
    }

    /**
     * 查询用户名是否已存在
     * @param userName
     * @return
     */
    private boolean userNameExit(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);

        //根据查询到的数据条数判断用户名是否已存在
        return count(queryWrapper)>0;
    }

    /**
     * 查询手机号是否已存在
     * @param phonenumber
     * @return
     */
    private boolean phonenumberExit(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phonenumber);

        //根据查询到的数据条数判断用户名是否已存在
        return count(queryWrapper)>0;
    }

    /**
     * 查询邮箱是否已存在
     * @param email
     * @return
     */
    private boolean emailExit(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);

        //根据查询到的数据条数判断用户名是否已存在
        return count(queryWrapper)>0;
    }
}




