package com.hjj.service.Impl;

import com.alibaba.fastjson.JSON;
import com.hjj.dao.pojo.SysUser;
import com.hjj.service.LoginService;
import com.hjj.service.SysUserService;
import com.hjj.utils.JwtUtils;
import com.hjj.vo.ErrorCode;
import com.hjj.vo.Result;
import com.hjj.vo.params.UserParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
   private final  String satl = "mszlu!@#";

    /**
     *   1.检查参数是否合法
     *   2.根据用户名去user表查询是否存在
     *   3.校验用户名和密码
     *   3.如果不存在则 登录失败
     *   4.如果校验成功 使用jwt生成token 返回给前端
     *   5.token放入redis中,redis   token:user信息 ,并设置过期时间(登录认证先认证token字符串是否合法,再去redis认证是否存在)
     * @return
     */
    @Override
    public Result login(UserParams params) {
        String account = params.getAccount();
        String password = params.getPassword();
        //1.
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.error(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //2.
        SysUser sysUser = sysUserService.findUser(account);
        //3.
        if (sysUser == null) {
            return Result.error(ErrorCode.ACCOUNT_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_NOT_EXIST.getMsg());
        }
        String realpwd = DigestUtils.md5Hex(password + sysUser.getSalt());  //加密密码
        if (sysUser.getPassword().equals(realpwd)){
            //4.
            String token = JwtUtils.createToken(sysUser.getId());
            redisTemplate.opsForValue().set("Token:" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
            return Result.success(token);
        }else {
            return Result.error(ErrorCode.ACCOUNT_PWD_ERROR.getCode(),ErrorCode.ACCOUNT_PWD_ERROR.getMsg());
        }

    }

    @Override
    public SysUser chackToken(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> map = JwtUtils.checkToken(token);
        if(map==null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("Token:"+token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

}
