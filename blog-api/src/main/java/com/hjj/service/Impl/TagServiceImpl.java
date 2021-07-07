package com.hjj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjj.dao.mapper.TagMapper;
import com.hjj.dao.pojo.Tag;
import com.hjj.service.TagService;
import com.hjj.vo.Result;
import com.hjj.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByArticleID(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleID(articleId);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        /**
         * 1.标签所拥有的的文章数量最多就是最热标签
         * 2.查询  根据tag_id分组计数,从大到小排列,取前limit个
         *
         */
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        //需求的是tagid和tagname 的tag对象
         List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result tags() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = tagMapper.selectList(wrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result tagsdetail() {

        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(tags);
    }

    @Override
    public Result tagsdetailById(Long id) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getId,id);
        Tag tag = tagMapper.selectById(id);

        return Result.success(copy(tag));
    }

    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }
}

