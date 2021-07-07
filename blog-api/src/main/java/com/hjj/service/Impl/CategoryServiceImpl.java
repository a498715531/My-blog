package com.hjj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjj.dao.mapper.CategoryMapper;
import com.hjj.dao.pojo.Category;
import com.hjj.service.CategoryService;
import com.hjj.vo.CategoryVo;
import com.hjj.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl  implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));

        return categoryVo;
    }

    @Override
    public Result categorys() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper();
        wrapper.select(Category::getId,Category::getAvatar);
        List<Category> categories = categoryMapper.selectList(wrapper);
        return Result.success(copylist(categories)) ;
    }

    @Override
    public Result categorysdetail() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());


        return Result.success(categories);
    }

    @Override
    public Result categorysdetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }


    private Object copylist(List<Category> categories) {
        List list = new ArrayList();
        for (Category category : categories) {
            list.add(copy(category));
        }
        return list;
    }

    private Object copy(Category categories) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(categories,categoryVo);
        categoryVo.setId(String.valueOf(categories.getId()));
        return categoryVo;
    }
}
