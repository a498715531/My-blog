package com.hjj.vo.params;

import lombok.Data;

@Data
public class UserParams {
    private String account;
    private  String password;
    private  String nickname;
    private  String salt;
}
