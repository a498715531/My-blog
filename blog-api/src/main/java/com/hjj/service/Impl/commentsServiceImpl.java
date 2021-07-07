package com.hjj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjj.dao.mapper.CommentMapper;
import com.hjj.dao.pojo.Comment;
import com.hjj.dao.pojo.SysUser;
import com.hjj.service.SysUserService;
import com.hjj.service.commentsService;
import com.hjj.utils.UserThreadLocal;
import com.hjj.vo.CommentVo;
import com.hjj.vo.Result;
import com.hjj.vo.UserVo;
import com.hjj.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class commentsServiceImpl implements commentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;


    @Override
    public Result commentsByArticleID(Long id) {
        /**
         * 1.根据文章id 查询 评论列表 从 comment表中查询
         * 2.根据作者id查询作者信息
         * 3.判断如果 level = 1 要去查询它有无子评论
         * 4.如果有 根据id进行查询(parent id)
         */
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Comment::getArticleId,id);
        wrapper.eq(Comment::getLevel,1);


        List<Comment> comments = commentMapper.selectList(wrapper);
        List<CommentVo> commentVoList = copylist(comments);




        return Result.success(commentVoList) ;
    }
   //评论
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentParam,comment);
        comment.setArticleId(Long.parseLong(commentParam.getArticleId()));
        comment.setAuthorId(sysUser.getId());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if(parent==null || parent ==0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        comment.setParentId(parent==null ? 0 :parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId==null ? 0 :toUserId);
        commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copylist(List<Comment> comments) {
        List<CommentVo> list = new ArrayList<>();
        for (Comment comment : comments) {
          list.add(copy(comment));
        }

        return list;
    }

    private CommentVo copy(Comment comment) {
         CommentVo commentVo = new CommentVo();

        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
    //作者信息
        Long AuthorId = comment.getAuthorId();
        UserVo userVo = sysUserService.FindUserVoByAuthorId(AuthorId);
        commentVo.setAuthor(userVo);

        //子评论
        Integer level = comment.getLevel();
        if(1 == level){
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to User 给谁评论
        if(level>1){
            Long id = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserById(id);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Comment::getParentId,id);
        wrapper.eq(Comment::getLevel,2);
        List<Comment> comments = commentMapper.selectList(wrapper);
        List<CommentVo> copylist = copylist(comments);
        return copylist;

    }
}
