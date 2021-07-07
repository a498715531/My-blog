package com.hjj.service;

import com.hjj.vo.Result;
import com.hjj.vo.params.CommentParam;

public interface commentsService {


    Result commentsByArticleID(Long id);

    Result comment(CommentParam commentParam);
}
