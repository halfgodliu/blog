package com.lyjava.domain.vos;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设置导出分类excel的列
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCategoryVo {

    @ExcelProperty("分类名")
    private String name;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("状态0:正常,1禁用")
    private String status;

}
