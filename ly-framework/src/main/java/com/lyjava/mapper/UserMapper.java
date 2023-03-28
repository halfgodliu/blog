package com.lyjava.mapper;

import com.lyjava.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 86198
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2023-02-23 16:32:59
* @Entity com.lyjava.domain.entity.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

}




