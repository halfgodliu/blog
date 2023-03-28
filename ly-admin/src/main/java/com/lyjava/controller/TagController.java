package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.TagDto;
import com.lyjava.domain.entity.Tag;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.domain.vos.TagVo;
import com.lyjava.enums.AppHttpCodeEnum;
import com.lyjava.service.TagService;
import com.lyjava.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
@Api(tags = "标签",description = "标签相关接口")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 获取标签列表的方法
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询标签列表")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagDto tagDto){
        return tagService.pageTagList(pageNum,pageSize, tagDto);
    }

    /**
     * 新增标签的方法
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增标签")
    public ResponseResult insertTag(@RequestBody Tag tag){
        boolean isSave = tagService.save(tag);
        return isSave ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 删除标签
     * @param id 要删除的标签id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除标签")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        boolean isRemove = tagService.removeById(id);
        return isRemove ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 通过id获取对应标签数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过id获取对应标签",notes = "可用于更新标签时获取对应标签数据")
    public ResponseResult getTagById(@PathVariable("id") Long id){
        //通过id查询，并转换成TagVo
        Tag tag = tagService.getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        //返回
        return ResponseResult.okResult(tagVo);
    }

    /**
     * 修改标签
     * @param tagDto
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改标签")
    public ResponseResult updateTag(@RequestBody TagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        boolean isUpdate = tagService.updateById(tag);
        return isUpdate ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 查询所有标签
     * @return
     */
    @GetMapping("/listAllTag")
    @ApiOperation(value = "查询所有标签")
    public ResponseResult listAllTag(){
        List<Tag> tags = tagService.list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tags, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}
