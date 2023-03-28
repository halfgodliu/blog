package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyjava.domain.vos.PageVo;

/**
* @author 86198
* @description 针对表【t_link(友链)】的数据库操作Service
* @createDate 2023-02-23 15:53:14
*/
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, String name, String status);
}
