package com.lyjava.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.domain.entity.ArticleTag;
import com.lyjava.service.ArticleTagService;
import com.lyjava.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author 86198
* @description 针对表【t_article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2023-02-28 11:56:34
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




