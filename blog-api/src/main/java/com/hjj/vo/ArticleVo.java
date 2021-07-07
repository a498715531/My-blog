package com.hjj.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hjj.dao.pojo.ArticleBody;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
//    @JsonSerialize(using = ToStringSerializer.class)
    private String id ;
    private  String title;
    private  String summary;
    private Integer commentCounts;
    private Integer viewCounts;
    private Integer weight;
    private String createDate;
    private String author;
    private ArticleBodyVo body;
    private List<TagVo> tags;
   private CategoryVo category;

}
