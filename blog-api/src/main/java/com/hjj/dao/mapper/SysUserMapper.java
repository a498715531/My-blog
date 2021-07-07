package com.hjj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjj.dao.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper  extends BaseMapper<SysUser> {
    SysUser FindUserByAccount(String account);

    SysUser FindUserBynickname(String nickname);

}
