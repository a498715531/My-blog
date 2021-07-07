package com.hjj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjj.dao.mapper.SysUserMapper;
import com.hjj.dao.pojo.Article;
import com.hjj.dao.pojo.SysUser;
import com.hjj.service.SysUserService;
import com.hjj.vo.UserVo;
import com.hjj.vo.params.UserParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser FindUserByArticleId(Long articleId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getId,articleId);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        return sysUser;
    }

    @Override
    public SysUser findUser(String account) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper();
        wrapper.eq(SysUser::getAccount,account);
  //      wrapper.eq(SysUser::getPassword,password);
        wrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getNickname,SysUser::getAvatar,SysUser::getSalt,SysUser::getPassword);
        wrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        return sysUser;
    }

    @Override
    public UserVo findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }

    @Override
    public SysUser findUserByAuthorId(Long authorId) {
        LambdaQueryWrapper<SysUser> wrapper =new LambdaQueryWrapper();
        wrapper.eq(SysUser::getId,authorId);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        wrapper.last("limit 1");

        return sysUser;
    }

    @Override
    public UserVo FindUserVoByArticleId(Long articleId) {
        SysUser sysUser = sysUserMapper.selectById(articleId);

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;

    }

    @Override
    public UserVo FindUserVoByAuthorId(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);

        UserVo userVo = new UserVo();
        userVo.setId(sysUser.getId().toString());
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }
}
