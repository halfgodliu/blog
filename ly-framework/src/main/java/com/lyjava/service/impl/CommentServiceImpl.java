package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Comment;
import com.lyjava.domain.vos.CommentVo;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.exception.SystemException;
import com.lyjava.service.CommentService;
import com.lyjava.mapper.CommentMapper;
import com.lyjava.service.UserService;
import com.lyjava.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author 86198
* @description 针对表【t_comment(评论表)】的数据库操作Service实现
* @createDate 2023-02-24 19:07:08
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{
    @Autowired
    private UserService userService;

    /**
     * 查询评论列表
     * @param commentType 评论类型，"0"表示文章评论，"1"表示友链评论
     * @param articleId 评论所属文章id
     * @param pageNum 评论页数
     * @param pageSize 每页评论数
     * @return
     */
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        //根据articleId查询
        //友链评论没有articleId，要判断是什么评论，在决定要不要根据articleId查询
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),
                Comment::getArticleId,articleId);
        //查询根评论id为-1（即本身为根评论）的评论
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_IS_ROOT);

        //根据评论类型进行查询
        queryWrapper.eq(Comment::getType,commentType);

        //分页查询
        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Comment> comments = page.getRecords();

        //调用自定义的方法将List<Comment>转换成List<CommentVo>
        List<CommentVo> commentVos = toCommentVoList(comments);

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVos){
            //调用自定义方法传入评论id查询子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //将查询到的子评论集合设置到CommentVo
            commentVo.setChildren(children);
        }

        //封装然后返回
        PageVo pageVo = new PageVo(commentVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 进行评论，添加评论
     * @param comment 评论的各种信息
     * @return
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        //在comment中有一些属性没有值，可以设置填充策略，
        //并在实体类的属性上添加@TableField(fill=???)注解应用填充策略自动添加

        //判断评论是否为空
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }

        //存入到数据库
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据评论的id查询所对应的子评论的集合
     * @param rootId 根评论id
     * @return
     */
    private List<CommentVo> getChildren(Long rootId){
        //根据评论id查询子评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,rootId);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> childrenComments = list(queryWrapper);

        //调用自定义的方法将List<Comment>转换成List<CommentVo>
        List<CommentVo> commentVos = toCommentVoList(childrenComments);

        return commentVos;
    }

    /**
     * 将List<Comment>转换成List<CommentVo>，获取根评论和获取子评论都需要，所以单独写出来提高复用
     * @param list 需要转换的List<Comment>
     * @return 返回List<CommentVo>
     */
    private List<CommentVo> toCommentVoList(List<Comment> list){
        //CommentVo中userName和toCommentUserName是Comment中没有的
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        for (CommentVo commentVo : commentVos){
            //根据createBy查询到nickName并设置到username
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            if (commentVo.getToCommentUserId()!=-1){//判断是否有所回复的评论
                //根据createBy查询到nickName并设置到userName
                String toNickName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toNickName);
            }
        }
        //返回
        return commentVos;
    }
}




