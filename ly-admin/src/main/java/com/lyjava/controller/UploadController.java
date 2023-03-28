package com.lyjava.controller;

import com.lyjava.domain.ResponseResult;
import com.lyjava.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传头像的方法
 */
@RestController
@RequestMapping
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片的方法
     * @param img 图片
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }
}
