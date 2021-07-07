package com.hjj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjj.service.TagService;
import com.hjj.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    private TagService tagService;


    //最热标签
   @GetMapping("hot")
    public Result hot(){
       //查询最热的6的标签
       int limit = 6;

       return  tagService.hots(limit);

   }
   //所有文章标签
    @GetMapping
    public  Result tags(){
       return tagService.tags();
    }
    //文章分类--标签
    @GetMapping("detail")
    public  Result tagsdetail(){
       return  tagService.tagsdetail();
    }

    @GetMapping("detail/{id}")
    public  Result tagsdetail(@PathVariable("id") Long id){
        return  tagService.tagsdetailById(id);
    }



}
