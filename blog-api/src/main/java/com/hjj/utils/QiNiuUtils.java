package com.hjj.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@Component
public class QiNiuUtils {
    @Value("${QiNiu.accessKey}")
    String accessKey;
    @Value("${QiNiu.secretKey}")
    String secretKey;
    @Value("${QiNiu.bucket}")
    String bucket;
    public static final String url = "http://qvnpyksev.hn-bkt.clouddn.com/";

    //构造一个带指定 Region 对象的配置类
    Configuration cfg = new Configuration(Region.huanan());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    //默认不指定key的情况下，以文件内容的hash值作为文件名


    public boolean upload(MultipartFile file, String fileName) {
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, fileName, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}