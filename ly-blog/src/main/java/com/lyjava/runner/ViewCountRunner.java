package com.lyjava.runner;

import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.entity.Article;
import com.lyjava.mapper.ArticleMapper;
import com.lyjava.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 实现CommandLineRunner接口，在应用启动时初始化缓存。
 */
@Component//要交给spring管理
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 在应用启动时把博客的浏览量存储到redis中
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        //查询博客信息，id、viewCount
        List<Article> articles = articleMapper.selectList(null);
        //使用stream流
        Map<String,Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(),
                        article -> article.getViewCount().intValue()));
        //存储到redis
        redisCache.setCacheMap(SystemConstants.REDIS_ARTICLE_VIEW,viewCountMap);
    }
}
