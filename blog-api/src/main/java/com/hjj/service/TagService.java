package com.hjj.service;

import com.hjj.vo.Result;
import com.hjj.vo.TagVo;

import java.util.List;

public interface TagService {
    //根据文章id查询标签列表
    List<TagVo> findTagsByArticleID(Long articleId);

    Result hots(int limit);

    Result tags();

    Result tagsdetail();

    Result tagsdetailById(Long id);
}
