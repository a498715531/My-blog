package com.hjj.controller;

import com.hjj.dao.pojo.SysUser;
import com.hjj.service.LoginService;
import com.hjj.service.UserService;
import com.hjj.utils.UserThreadLocal;
import com.hjj.vo.Result;
import com.hjj.vo.params.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
public class UserController {
     @Autowired
    private LoginService loginService;
     @Autowired
     private UserService userService;


    @PostMapping("login")
    public Result login(@RequestBody UserParams params){

        return loginService.login(params);
    }

    @GetMapping("users/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return   userService.findUserByToken(token);

    }
    @GetMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token){
        return userService.logout(token);
    }

    @PostMapping("register")
    public  Result register(@RequestBody SysUser params){
        return userService.register(params);
    }

}
