package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.dto.AddArticleDto;
import com.lyjava.domain.entity.Article;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.ArticleTag;
import com.lyjava.domain.entity.Category;
import com.lyjava.domain.vos.*;
import com.lyjava.service.ArticleService;
import com.lyjava.mapper.ArticleMapper;
import com.lyjava.service.ArticleTagService;
import com.lyjava.service.CategoryService;
import com.lyjava.utils.BeanCopyUtils;
import com.lyjava.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author 86198
* @description 针对表【t_article】的数据库操作Service实现
* @createDate 2023-02-21 14:52:12
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 查询热门文章（必须是已发布文章，按照浏览量排序，值查询前面10条）
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //查询已发布的文章，status为0
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照viewCount降序排
        queryWrapper.orderByDesc(Article::getViewCount);
        //查询前面10条
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();

        //从redis中查询浏览量，这里是后来添加的（这样可能会导致浏览量排序出现问题，这里先不管）
        for (Article article : articles){
            Integer viewCount =
                    redisCache.getCacheMapValue(SystemConstants.REDIS_ARTICLE_VIEW, String.valueOf(article.getId()));
            article.setViewCount(Long.valueOf(viewCount));
        }

        //热门文章只需要id，title（标题），viewCount（浏览量），因此不需要返回一个完整的Article类回去
        //可以定义一个值包含热门文章需要的属性的类，然后将查询到的数据拷贝到该类中去
        //使用自定义的工具类完成bean拷贝
        List<HotArticle> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticle.class);

        return ResponseResult.okResult(articleVos);
    }

    /**
     * 查询文章列表（首页查询和按文章分类查询都是通过该方法）
     * @param pageNum 第几页
     * @param pageSize 每页数量
     * @param categoryId 文章分类id
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();

        //查询categoryName，并设置到Article实例对象中
        for (Article article : articles){
            Long articleCategoryId = article.getCategoryId();
            Category category = categoryService.getById(articleCategoryId);
            article.setCategoryName(category.getName());
            //从redis中查询浏览量，这里是后来添加的
            Integer viewCount =
                redisCache.getCacheMapValue(SystemConstants.REDIS_ARTICLE_VIEW, String.valueOf(article.getId()));
            article.setViewCount(Long.valueOf(viewCount));
        }
        //使用stream流
        /*articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());*/

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        //前端要求返回的数据里面包含rows和total属性，所以自定义了一个pageVo类包含这两个属性
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    /**
     * 返回文章详情
     * @param id 文章id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //浏览量从redis中查询
        Integer viewCount =
                redisCache.getCacheMapValue(SystemConstants.REDIS_ARTICLE_VIEW, String.valueOf(id));
        article.setViewCount(Long.valueOf(viewCount));
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        if (category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }

        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 更新浏览量（这里更新的是redis中，数据库通过定时任务更新）
     * @param id 文章id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应文章的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.REDIS_ARTICLE_VIEW,id.toString(),1);

        return ResponseResult.okResult();
    }

    /**
     * 写（添加）博文
     * @param addArticleDto
     * @return
     */
    @Override
    @Transactional//添加事务
    public ResponseResult add(AddArticleDto addArticleDto) {
        //转换成Article
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        //将博文保存到数据库
        save(article);

        //将博文和标签的关联关系保存到数据库
        List<Long> tags = addArticleDto.getTags();
        //获取博文和标签的关联关系（可以是一对多）
        List<ArticleTag> articleTags = tags.stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    /**
     * 分页查询文章列表
     * @param pageNum
     * @param pageSize
     * @param title
     * @param summary
     * @return
     */
    @Override
    public ResponseResult<PageVo> pageList(Integer pageNum, Integer pageSize, String title, String summary) {
        //查询参数
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        queryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //封装返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 更新时获取文章内容
     * @param id
     * @return
     */
    @Override
    public ResponseResult<ArticleVo> getContent(Long id) {
        //根据id获取文章内容
        Article article = getById(id);
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);

        //查询文章拥有的标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.getBaseMapper().selectList(queryWrapper);
        List<Long> tags = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());

        //将查询到的tags存入到ArticleVo的tags属性中
        articleVo.setTags(tags);
        //返回
        return ResponseResult.okResult(articleVo);
    }

    /**
     * 执行更新操作
     * @param articleVo
     * @return
     */
    @Override
    @Transactional//开启事务
    public ResponseResult updateArticle(ArticleVo articleVo) {
        //更新文章表
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);
        updateById(article);

        //将博文和标签的关联关系保存到数据库
        List<Long> tags = articleVo.getTags();
        //先将删除本篇文章旧数据
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,articleVo.getId());
        articleTagService.remove(queryWrapper);
        //获取博文和标签的关联关系（可以是一对多）
        List<ArticleTag> articleTags = tags.stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    /**
     * 删除文章的方法
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResponseResult deleteArticel(Long id) {
        //删除文章
        removeById(id);

        //删除文章和标签关联
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        articleTagService.remove(queryWrapper);

        return ResponseResult.okResult();
    }
}




