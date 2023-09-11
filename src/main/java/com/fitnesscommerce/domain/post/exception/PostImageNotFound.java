package com.fitnesscommerce.domain.post.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class PostImageNotFound extends GlobalException {
    private static final String MESSAGE = "이미지를 찾을 수 없습니다.";

    public PostImageNotFound(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode(){
        return 404;
    }
}
