package com.lyjava.domain.vos;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 后台系统更新文章时要先查询文章数据，查到的数据存放到这，
 * 也可用于接收更新时的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {
    /**
     *
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 所属分类id
     */
    private Long categoryId;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否置顶（0否，1是）
     */
    private String isTop;

    /**
     * 状态（0已发布，1草稿）
     */
    private String status;

    /**
     * 访问量
     */
    private Long viewCount;

    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

    /**
     * 标签
     */
    private List<Long> tags;
}
