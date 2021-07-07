package com.hjj.service.Impl;

import com.hjj.service.UploadService;
import com.hjj.utils.QiNiuUtils;
import com.hjj.vo.ErrorCode;
import com.hjj.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;
@Service
public class uploadServiceImpl  implements UploadService {

    @Resource
  private   QiNiuUtils qiNiuUtils;
    @Override
    public Result upload(@RequestParam("image") MultipartFile file) {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //获得唯一的文件名称
        String FileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename,".");
        boolean upload = qiNiuUtils.upload(file, FileName);
        if(upload){
            return Result.success(qiNiuUtils.url+FileName);
        }
        return Result.error(7001,"文件上传失败");
    }
}
