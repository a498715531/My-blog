package com.hjj.service;

import com.hjj.dao.pojo.SysUser;
import com.hjj.vo.UserVo;
import netscape.security.Principal;
import org.springframework.web.bind.annotation.PathVariable;


public interface SysUserService {
   SysUser FindUserByArticleId(Long articleId);

    SysUser findUser(String account);

    UserVo findUserById(@PathVariable("id") Long id);

    SysUser findUserByAuthorId(Long authorId);

    UserVo FindUserVoByArticleId(Long articleId);

    UserVo FindUserVoByAuthorId(Long authorId);
}
