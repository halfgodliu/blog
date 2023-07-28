package com.lyjava.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.entity.Article;
import com.lyjava.service.ArticleService;
import com.lyjava.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时任务类：注意要在配置类（或启动类）上加上@EnableScheduling
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    /**
     * 定时将redis中的文章浏览量更新到数据库中
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void updateViewCount(){
        //获取redis中的数据
        Map<String, Integer> cacheMap = redisCache.getCacheMap(SystemConstants.REDIS_ARTICLE_VIEW);
        //将获取的数据封装，（注意redis中获取的数据类型和需要的数据类型不一样，需要转换）
        /*List<Article> articles = cacheMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());*/
        //更新数据库
        //articleService.updateBatchById(articles);

        //更新到数据库
        for (Map.Entry<String,Integer> entry : cacheMap.entrySet()){
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getId,entry.getKey());
            updateWrapper.set(Article::getViewCount,entry.getValue());

            articleService.update(updateWrapper);
        }
    }
}
