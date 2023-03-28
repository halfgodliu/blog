package com.lyjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyjava.constants.SystemConstants;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Category;
import com.lyjava.domain.entity.Link;
import com.lyjava.domain.vos.LinkVo;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.service.LinkService;
import com.lyjava.mapper.LinkMapper;
import com.lyjava.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author 86198
* @description 针对表【t_link(友链)】的数据库操作Service实现
* @createDate 2023-02-23 15:53:14
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService{

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        //查询状态为审核通过状态的友链
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList = list(queryWrapper);
        //转换成VO
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    /**
     * 分页查询友链
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @Override
    public ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, String name, String status) {
        //查询条件
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);
        //分页查询
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //封装返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}




