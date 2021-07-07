package com.hjj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjj.dao.pojo.Article;
import com.hjj.vo.dos.Archives;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ArticleMapper  extends BaseMapper<Article> {

    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);

    List<Article> selectCommentCount(Long ArticleId);
}
