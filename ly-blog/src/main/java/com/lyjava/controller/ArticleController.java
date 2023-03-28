package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /*@GetMapping("/test")
    public String test1(){
        List<Article> list = articleService.list();
        return list.toString();
    }*/

    /**
     * 查询热门文章
     * @return
     */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    /**
     * 查询文章列表（首页查询和按文章分类查询都是通过该方法）
     * @param pageNum 第几页
     * @param pageSize 每页数量
     * @param categoryId 文章分类id
     * @return
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    /**
     * 返回文章详情
     * @param id 文章id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
     * 更新浏览量（这里更新的是redis中，数据库通过定时任务更新）
     * @param id 文章id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
