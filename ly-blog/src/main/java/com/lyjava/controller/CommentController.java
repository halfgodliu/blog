package com.lyjava.controller;

import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.AddCommentDto;
import com.lyjava.domain.entity.Comment;
import com.lyjava.service.CommentService;
import com.lyjava.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 查询评论相关
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 查询文章评论列表
     * @param articleId 评论所属文章id
     * @param pageNum 评论页数
     * @param pageSize 每页评论数
     * @return
     */
    @GetMapping("/commentList")
    @ApiOperation(value = "评论列表",notes = "获取指定文章一页评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId",value = "文章id"),
            @ApiImplicitParam(name = "pageNum",value = "第几页"),
            @ApiImplicitParam(name = "pageSize",value = "每页评论数量")
    })
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    /**
     * 进行评论，添加评论
     * @param addCommentDto 评论的各种信息
     * @return
     */
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    /**
     * 查询友链评论列表
     * @param pageNum 友链评论页数
     * @param pageSize 友链每页评论数
     * @return
     */
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
