package com.hjj.vo;

import lombok.Data;

@Data
public class UserVo {
    private String id;
    private String account;
    private String nickname;
    private String avatar;
    private  String salt;
}
