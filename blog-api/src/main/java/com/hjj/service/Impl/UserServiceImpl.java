package com.hjj.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjj.dao.mapper.SysUserMapper;
import com.hjj.dao.pojo.Article;
import com.hjj.dao.pojo.SysUser;
import com.hjj.service.LoginService;
import com.hjj.service.UserService;
import com.hjj.utils.JwtUtils;
import com.hjj.vo.ErrorCode;
import com.hjj.vo.Result;
import com.hjj.vo.UserVo;
import com.hjj.vo.params.UserParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataUnit;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional  //事务
public class UserServiceImpl implements UserService {

    @Autowired
    private LoginService loginService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    UserService userService;



    /**
     *   1.验证token合法性, jwt是否能解析成功token, redis中是否存在
     *
     * @return
     */
    @Override
    public Result findUserByToken(String token) {
        SysUser sysUser = loginService.chackToken(token);
        if(sysUser == null ){
            return Result.error(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        UserVo userVo = new UserVo();
        userVo.setNickname(sysUser.getNickname());
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setId(String.valueOf(sysUser.getId()));
        userVo.setAccount(sysUser.getAccount());



        return Result.success(userVo);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("Token:"+token);

        return Result.success(null);
    }

    /**
     * 注册
     * 1.根据用户名查找数据库 如果有:返回用户名已存在;
     * 2.查昵称,如果有:返回昵称已存在
     * 3.insert用户
     * 4.生成token ,存入redis并返回
     * 6,加上事务
     *
     * @return
     */
    @Override
    public Result register(SysUser sysUser) {
        String account = sysUser.getAccount();


        //1.根据用户名查找数据库 如果有:返回用户名已存在;
        SysUser userByAccount = sysUserMapper.FindUserByAccount(sysUser.getAccount());
        if(!ObjectUtils.isEmpty(userByAccount)){
            return Result.error(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //2.查昵称,如果有:返回昵称已存在
       SysUser userBynickname= sysUserMapper.FindUserBynickname(sysUser.getNickname());
        if(!ObjectUtils.isEmpty(userBynickname)){
            return Result.error(ErrorCode.NICKNAME_EXIST.getCode(),ErrorCode.NICKNAME_EXIST.getMsg());
        }
        //3.insert用户
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setDeleted(0);
        String salt = String.valueOf(RandomUtils.nextInt(1000,1000000));
        sysUser.setSalt(salt);
        sysUser.setPassword(DigestUtils.md5Hex((sysUser.getPassword()+salt)));
        int insert = sysUserMapper.insert(sysUser);
        if(insert==1){
        //4.生成token ,存入redis并返回
            String token = JwtUtils.createToken(sysUser.getId());
            redisTemplate.opsForValue().set("Token:"+token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
            return Result.success(token);

        }
        return null;
    }


}
