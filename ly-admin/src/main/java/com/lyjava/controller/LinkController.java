package com.lyjava.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyjava.domain.ResponseResult;
import com.lyjava.domain.entity.Link;
import com.lyjava.domain.vos.PageVo;
import com.lyjava.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 分页查询友链的方法
     * @param pageNum
     * @param pageSize
     * @param name
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, String name, String status){
        return linkService.listPage(pageNum,pageSize,name,status);
    }

    /**
     * 新增友链的接口
     * @param link
     * @return
     */
    @PostMapping
    public ResponseResult insert(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    /**
     * 新增前先通过id查询友链
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable Long id){
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    /**
     * 修改友链的接口
     * @param link
     * @return
     */
    @PutMapping()
    public ResponseResult update(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    /**
     * 通过id删除友链
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 改变友链状态
     * @param link
     * @return
     */
    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }
}
