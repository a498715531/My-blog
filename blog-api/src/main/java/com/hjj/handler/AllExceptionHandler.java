package com.hjj.handler;

import com.hjj.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常处理
@ControllerAdvice
//对加了controller注解方法进行拦截处理  aop
public class AllExceptionHandler {
    //进行异常处理,处理EException.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.error(-999,"系统异常,请联系管理员");
    }
}
