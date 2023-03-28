package com.lyjava.mapper;

import com.lyjava.domain.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 86198
* @description 针对表【t_article】的数据库操作Mapper
* @createDate 2023-02-21 14:52:12
* @Entity com.lyjava.domain.entity.Article
*/
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

}




