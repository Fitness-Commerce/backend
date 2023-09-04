package com.fitnesscommerce.domain.post.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class PostCommentNotFound extends GlobalException {

    private static final String MESSAGE = "해당 댓글을 찾을 수 없습니다.";

    public PostCommentNotFound(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode(){
        return 404;
    }
}
