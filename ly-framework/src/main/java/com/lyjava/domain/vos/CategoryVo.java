package com.lyjava.domain.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回给前端的文章分类数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    private Long id;
    private String name;
    /**
     * 分类描述
     */
    private String description;
}
