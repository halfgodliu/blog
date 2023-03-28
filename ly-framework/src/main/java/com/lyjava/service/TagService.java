package com.lyjava.service;

import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.dto.TagDto;
import com.lyjava.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyjava.domain.vos.PageVo;

/**
* @author 86198
* @description 针对表【t_tag(标签)】的数据库操作Service
* @createDate 2023-02-26 17:15:20
*/
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagDto tagDto);

}
