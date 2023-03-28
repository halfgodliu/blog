package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyjava.domain.vos.PageVo;

/**
* @author 86198
* @description 针对表【t_category(分类表)】的数据库操作Service
* @createDate 2023-02-22 20:11:29
*/
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, String name, String status);

}
