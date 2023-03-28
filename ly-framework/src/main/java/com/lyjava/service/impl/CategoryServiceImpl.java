package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Article;
import com.lyjava.domain.entity.Category;
import com.lyjava.domain.vos.CategoryVo;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.service.ArticleService;
import com.lyjava.service.CategoryService;
import com.lyjava.mapper.CategoryMapper;
import com.lyjava.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 86198
* @description 针对表【t_category(分类表)】的数据库操作Service实现
* @createDate 2023-02-22 20:11:29
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表  状态为已发布的文章
        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();
        articleQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(articleQueryWrapper);

        //获取文章的分类id，并且去重（set集合无法重复）
        Set<Long> categoryIds = articles.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        //查询分类表，status（状态）为0（正常）
        List<Category> categories = listByIds(categoryIds);
        //使用stream过滤状态为0的分类
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 分页查询分类列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @Override
    public ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, String name, String status) {
        //查询条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        //分页查询
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //封装返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}




