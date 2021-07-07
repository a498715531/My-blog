package com.hjj.service;

import com.hjj.dao.pojo.SysUser;
import com.hjj.vo.Result;
import com.hjj.vo.params.UserParams;

public interface UserService {
    Result findUserByToken(String token) ;


    Result logout(String token);

    Result register(SysUser params);

}
