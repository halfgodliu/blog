package com.lyjava.domain.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回给前端的分页数据封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {
    /**
     * 将返回给前端的文章数据放到rows中
     */
    private List rows;

    /**
     * total为查询到的总记录数据
     */
    private Long total;
}
