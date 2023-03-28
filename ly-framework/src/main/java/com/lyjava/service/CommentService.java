package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86198
* @description 针对表【t_comment(评论表)】的数据库操作Service
* @createDate 2023-02-24 19:07:08
*/
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
