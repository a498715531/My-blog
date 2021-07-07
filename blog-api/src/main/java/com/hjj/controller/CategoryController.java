package com.hjj.controller;

import com.hjj.service.CategoryService;
import com.hjj.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {
        @Autowired
        private  CategoryService categoryService;
    @GetMapping
    public Result categorys(){
        return categoryService.categorys();
    }
    //文章分类
    @GetMapping("detail")
    public Result  categorysdetail(){
      return   categoryService.categorysdetail();
    }

    @GetMapping("detail/{id}")
    public Result  categorysdetailById(@PathVariable("id")Long id) {
        return   categoryService.categorysdetailById(id);
    }
}
