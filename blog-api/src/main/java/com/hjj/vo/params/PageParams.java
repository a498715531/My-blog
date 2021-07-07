package com.hjj.vo.params;

import lombok.Data;
@Data
public class PageParams {
    private  byte page = 1;
    private  byte pageSize =10;
    private Long categoryId;
    private Long tagId;
    private String year;
    private String month;

    public String getMonth(){
        if(this.month !=null && this.month.length() ==1){
            return "0"+this.month;
        }
        return this.month;
    }
}
