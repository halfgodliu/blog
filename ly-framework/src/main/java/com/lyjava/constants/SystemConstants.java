package com.lyjava.constants;

/**
 * 实际项目中都不允许直接在代码中使用字面值。都需要定义成常量来使用。这种方式有利于提高代码的可维护性。
 */
public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     *  状态是正常状态
     */
    public static final String STATUS_NORMAL = "0";

    /**
     *  友链是审核通过状态
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     *  评论为根评论
     */
    public static final Long COMMENT_IS_ROOT = -1L;

    /**
     *  文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     *  友链评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     *  redis中存放文章浏览量的key
     */
    public static final String REDIS_ARTICLE_VIEW = "article:viewCount";

    /**
     * redis存入前台用户数据时key的前缀
     */
    public static final String REDIS_BLOGLOGIN_PRE = "bloglogin:";
    /**
     * redis存入后台用户数据时key的前缀
     */
    public static final String REDIS_ADMINLOGIN_PRE = "adminlogin:";

    /**
     * 菜单类型为C（菜单）
     */
    public static final String MENU = "C";
    /**
     * 菜单类型为F（按钮）
     */
    public static final String BUTTON = "F";

    /**
     * 用户类型是管理员
     */
    public static final String ADMIN = "1";
}