package com.hjj.service;

import com.hjj.vo.CategoryVo;
import com.hjj.vo.Result;

import java.util.List;

public interface CategoryService {
   Result categorys();



    CategoryVo findCategoryById(Long categoryId);

    Result categorysdetail();

    Result categorysdetailById(Long id);
}
