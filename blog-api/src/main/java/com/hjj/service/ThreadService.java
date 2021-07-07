package com.hjj.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hjj.dao.mapper.ArticleMapper;
import com.hjj.dao.mapper.CommentMapper;
import com.hjj.dao.pojo.Article;
import com.hjj.dao.pojo.Comment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ThreadService {


    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        Integer viewCounts = article.getViewCounts();
        Article articleupdate = new Article();
        articleupdate.setViewCounts(viewCounts +1);
        LambdaUpdateWrapper<Article> wrapper =new LambdaUpdateWrapper();
        wrapper.eq(Article::getId,article.getId());
        wrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(articleupdate,wrapper);
    }
    @Async("taskExecutor")
    public void updateArticleCommentCount( ArticleMapper articleMapper , CommentMapper commentMapper,Article  article) {

         List<Article> list = articleMapper.selectCommentCount(article.getId());
         article.setCommentCounts(list.size());
         articleMapper.updateById(article);

    }
}
