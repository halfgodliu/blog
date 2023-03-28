package com.lyjava.service;

import com.lyjava.domain.dto.AddArticleDto;
import com.lyjava.domain.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.vos.ArticleVo;
import com.lyjava.domain.vos.PageVo;

/**
* @author 86198
* @description 针对表【t_article】的数据库操作Service
* @createDate 2023-02-21 14:52:12
*/
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto addArticleDto);

    ResponseResult<PageVo> pageList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult<ArticleVo> getContent(Long id);

    ResponseResult updateArticle(ArticleVo articleVo);

    ResponseResult deleteArticel(Long id);
}
