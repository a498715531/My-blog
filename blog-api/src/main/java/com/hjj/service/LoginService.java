package com.hjj.service;

import com.hjj.dao.pojo.SysUser;
import com.hjj.vo.Result;
import com.hjj.vo.params.UserParams;

public interface LoginService {


    Result login(UserParams params);

    SysUser chackToken(String Token);
}
