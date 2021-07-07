package com.hjj.controller;


import com.hjj.service.UploadService;
import com.hjj.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class uploadController {

    @Autowired
    private UploadService uploadService;
    /**
     * 图片上传
     */
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
        return  uploadService.upload(file);
    }
}
