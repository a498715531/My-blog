package com.hjj.vo.params;

import lombok.Data;

@Data
public class CommentParam {

    private String articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
