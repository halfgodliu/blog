package com.lyjava.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用来接收前端传来的查询tag列表的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {

    private Long id;
    private String name;
    private String remark;
}
