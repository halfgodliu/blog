package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.TagDto;
import com.lyjava.domain.entity.Tag;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.domain.vos.TagVo;
import com.lyjava.service.TagService;
import com.lyjava.mapper.TagMapper;
import com.lyjava.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author 86198
* @description 针对表【t_tag(标签)】的数据库操作Service实现
* @createDate 2023-02-26 17:15:20
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    /**
     * 获取标签列表的方法，可能还有查询的参数
     * @return
     */
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagDto tagDto) {
        //查询条件
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        //要判断是否有参数，有才加上参数查询
        queryWrapper.like(StringUtils.hasText(tagDto.getName()),Tag::getName, tagDto.getName());
        queryWrapper.like(StringUtils.hasText(tagDto.getRemark()),Tag::getRemark, tagDto.getRemark());
        //分页查询
        Page<Tag> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //将List<Tag>转换成List<TagVo>
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(page.getRecords(), TagVo.class);

        //封装数据返回
        PageVo pageVo = new PageVo(tagVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }


}




