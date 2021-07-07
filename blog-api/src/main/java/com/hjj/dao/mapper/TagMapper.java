package com.hjj.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjj.dao.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findTagsByArticleID(Long articleId);


    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds( List<Long> tagIds);
}
