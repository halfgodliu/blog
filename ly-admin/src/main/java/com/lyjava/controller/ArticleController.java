package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.AddArticleDto;
import com.lyjava.domain.vos.ArticleVo;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 写（添加）博文的方法
     * @param addArticleDto
     * @return
     */
    @PostMapping
    @ApiOperation(value = "写（添加）博文")
    public ResponseResult add(@RequestBody AddArticleDto addArticleDto){
        return articleService.add(addArticleDto);
    }


    /**
     * 查询文章列表
     * @param pageNum
     * @param pageSize
     * @param title
     * @param summary
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询文章列表")
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    public ResponseResult<PageVo> pageList(Integer pageNum,Integer pageSize,String title,String summary){
        return articleService.pageList(pageNum,pageSize,title,summary);
    }

    /**
     * 更新时获取文章内容
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "更新时获取文章详情内容")
    public ResponseResult<ArticleVo> getContent(@PathVariable("id") Long id){
        return articleService.getContent(id);
    }

    /**
     * 真正执行更新的接口
     * @param articleVo
     * @return
     */
    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleVo articleVo){
        return articleService.updateArticle(articleVo);
    }

    /**
     * 删除文章的方法
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除文章")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticel(id);
    }
}
