package com.hjj.controller;

import com.hjj.service.commentsService;
import com.hjj.vo.Result;
import com.hjj.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
  @Autowired
  private commentsService commentsService;




    //获取评论列表
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id ){


        return commentsService.commentsByArticleID(id);
    }
   //评论
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){

      return commentsService.comment(commentParam);
    }
}
