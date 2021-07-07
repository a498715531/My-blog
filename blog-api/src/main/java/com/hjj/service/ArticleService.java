package com.hjj.service;

import com.hjj.vo.Result;
import com.hjj.vo.params.ArticleParam;
import com.hjj.vo.params.PageParams;

public interface ArticleService {

   Result listArticle(PageParams pageParams);

    Result hotArticle(int limit);


    Result newArticle(int limit);

    Result listArchives();

    Result findArticleById(Long id);

    Result publish(ArticleParam articleParam);
}
