package com.lyjava.domain.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门文章处发送请求后，返回给前端的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticle {
    private Long id;
    private String title;
    private Long viewCount;
}
