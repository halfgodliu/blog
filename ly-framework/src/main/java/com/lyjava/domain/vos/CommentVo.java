package com.lyjava.domain.vos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    /**
     *
     */
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 根评论id
     */
    private Long rootId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 所回复的目标评论的userid
     */
    private Long toCommentUserId;
    /**
     * 所回复的目标评论的userName
     */
    private String toCommentUserName;

    /**
     * 回复目标评论id
     */
    private Long toCommentId;

    /**
     * 创建本评论的用户id
     */
    private Long createBy;
    /**
     * 创建本评论的用户昵称（本来应该写成nickName）
     */
    private String username;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 子评论
     */
    private List<CommentVo> children;
}
