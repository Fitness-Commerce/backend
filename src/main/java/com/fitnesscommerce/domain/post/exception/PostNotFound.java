package com.fitnesscommerce.domain.post.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class PostNotFound extends GlobalException {

    private static final String MESSAGE = "게시글을 찾을 수 없습니다.";

    public PostNotFound(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode(){
        return 404;
    }
}
