package com.lyjava.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Category;
import com.lyjava.domain.vos.CategoryVo;
import com.lyjava.domain.vos.ExcelCategoryVo;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.service.CategoryService;
import com.lyjava.utils.BeanCopyUtils;
import com.lyjava.utils.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
@Api(tags = "分类接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类
     * @return
     */
    @GetMapping("/listAllCategory")
    @ApiOperation(value = "查询所有分类")
    public ResponseResult<CategoryVo> listAllCategory(){
        List<Category> categories = categoryService.list();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 导出excel的方法
     * @param response
     */
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    @ApiOperation(value = "导出excel的接口")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);

            //获取需要导出的数据
            List<Category> categories = categoryService.list();
            //设置（转换）需要导出的列
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);

            //把数据写入到excel中
            EasyExcel.write(response.getOutputStream(),ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (IOException e) {
            //如果出现异常也要响应json
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response,JSON.toJSONString(responseResult));
        }
    }

    /**
     * 分页查询分类列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.listPage(pageNum,pageSize,name,status);
    }

    /**
     * 新增分类的接口
     * @param category
     * @return
     */
    @PostMapping
    public ResponseResult insert(@RequestBody Category category){
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    /**
     * 新增前先通过id查询分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id){
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }

    /**
     * 修改分类的接口
     * @param category
     * @return
     */
    @PutMapping()
    public ResponseResult update(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    /**
     * 通过id删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }
}
