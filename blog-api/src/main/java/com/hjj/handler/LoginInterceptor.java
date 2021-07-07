package com.hjj.handler;

import com.alibaba.fastjson.JSON;
import com.hjj.dao.pojo.SysUser;
import com.hjj.service.LoginService;
import com.hjj.utils.UserThreadLocal;
import com.hjj.vo.ErrorCode;
import com.hjj.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行controller方法(handler)之前进行执行
        /**
         * 1.需要判断 请求的接口路径 是否为controller方法
         * 2.谈判token是否为空
         * 3.如果token不为空 调用loginservice check Token
         * 4.认证成功 放行
         */
        if(!(handler instanceof HandlerMethod)){
            //如果访问的不是controller方法则放行
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("======================request go!====================");
        log.info("url:{}",request.getRequestURI());
        log.info("methon:{}",request.getMethod());
        log.info("token:{}",token);
        log.info("======================request end!====================");
    //2.
        if(StringUtils.isBlank(token)){
            Result result = Result.error(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;

        }
     //3.
        SysUser sysUser = loginService.chackToken(token);
        if(sysUser ==null){
            Result result = Result.error(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
       // 4.认证成功 放行
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
