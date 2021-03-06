package com.hjj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjj.dao.mapper.*;
import com.hjj.dao.pojo.*;
import com.hjj.service.*;
import com.hjj.utils.UserThreadLocal;
import com.hjj.vo.*;
import com.hjj.vo.dos.Archives;
import com.hjj.vo.params.ArticleParam;
import com.hjj.vo.params.PageParams;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    CommentMapper commentMapper;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 1.???????????? article???
//         */
//        Page<Article> page = new Page(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if(pageParams.getCategoryId() !=null){
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> ArticleIdlist = new ArrayList<>();
//        if(pageParams.getTagId() != null){
//            //???????????????????????? article_tag
//            LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper();
//            wrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
//            for (ArticleTag articleTag : articleTags) {
//                ArticleIdlist.add(articleTag.getArticleId());
//            }
//            if(ArticleIdlist.size()>0){
//                queryWrapper.in(Article::getId,ArticleIdlist);
//            }
//        }

//        //????????????,??????????????????
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);

       // List list = articleMapper.hotArticle();

        return Result.success(copyList(articles,false,false));
    }

    /**
     * ???????????? ??????????????????
     * @param limit
     * @return
     */
    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);

        // List list = articleMapper.hotArticle();

        return Result.success(copyList(articles,false,false));

    }

    /**
     * ??????---????????????
     * @return
     */
    @Override
    public Result listArchives() {
        List<Archives> archives = articleMapper.listArchives();

        return Result.success(archives) ;
    }

    @Override
    public Result findArticleById(Long articleid) {
        /**
         * 1.??????id??????????????????
         * 2.??????bodyid???categoryid  ??????????????????
         */
        Article article = articleMapper.selectById(articleid);
        //??????????????????
       threadService.updateArticleViewCount(articleMapper,article);
        //??????????????????
        threadService.updateArticleCommentCount(articleMapper,commentMapper,article);

        ArticleVo articleVo = copy(article, true, true,true,true);
        return Result.success(articleVo);
    }

    /**
     * ????????????  ????????????id;
     * 1.body,
     * 2.tag
     * 3.category
     *
     *
     * @param articleParam
     * @return
     */
    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();  //????????????????????????
        Article article = new Article();
        article.setWeight(Article.Article_Common); //????????????
        article.setAuthorId(sysUser.getId());  //??????id = ??????????????????id
        article.setCreateDate(System.currentTimeMillis());  //???????????? ??????????????????
        article.setViewCounts(0);  //????????????
        article.setTitle(articleParam.getTitle()); //????????????
        article.setCommentCounts(0); //???????????? |
        article.setSummary(articleParam.getSummary()); // ????????????
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        articleMapper.insert(article);


        //2.tag
        List<TagVo> tags = articleParam.getTags();
        if(tags!= null) {
            for (TagVo tagVo : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tagVo.getId()));
                articleTagMapper.insert(articleTag);
            }
        }


        //1.body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);


         Map<String,String> map = new HashMap<>();
         map.put("id",article.getId().toString());
        return Result.success(map);
    }


    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> list = new ArrayList<>();
        for (Article record : records) {
            list.add(copy(record,isTag,isAuthor,false,false));
        }
        return list;

    }
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> list = new ArrayList<>();
        for (Article record : records) {
            list.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return list;

    }

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory ){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleID(articleId));
        }
        if(isAuthor){
            Long AuthorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserByAuthorId(AuthorId).getNickname());
        }
        if(isBody){
            Long bodyId = article.getBodyId();
           articleVo.setBody(findArticleBodyById(bodyId));
        }
        if(isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
