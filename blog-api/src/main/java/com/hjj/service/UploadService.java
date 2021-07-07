package com.hjj.service;

import com.hjj.vo.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Result upload(MultipartFile file);
}
