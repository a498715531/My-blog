package com.hjj.controller;

import com.hjj.common.aop.LogAnnotation;
import com.hjj.common.cache.Cache;
import com.hjj.service.ArticleService;
import com.hjj.service.ThreadService;
import com.hjj.vo.ArticleVo;
import com.hjj.vo.Result;
import com.hjj.vo.params.ArticleBodyParam;
import com.hjj.vo.params.ArticleParam;
import com.hjj.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 文章列表
     * @param pageParams
     * @return
     */
     @PostMapping
     @LogAnnotation(module = "文章",operator = "获取文章列表")
   //  @Cache(expire = 5*60*1000 , name = "listArticle")
    public Result  listArticle(@RequestBody PageParams pageParams){
    return  articleService.listArticle(pageParams);
    }

    /**
     * 最热文章
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5*60*1000 , name = "hotArticle")
    public Result hotArticle( ){
         int limit = 5;
         return articleService.hotArticle(limit);
    }
    /**
     * 首页--最新文章
     */
    @PostMapping("new")
    public Result newArticle(){
        int limit = 5;
        return articleService.newArticle(limit);
    }

    /**
     * 首页--文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }


    //文章详情
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id){

        return  articleService.findArticleById(id);
    }
    //发布文章
    @PostMapping("publish")
    public   Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);

    }

}
