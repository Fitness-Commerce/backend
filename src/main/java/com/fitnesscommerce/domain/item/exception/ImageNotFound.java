package com.fitnesscommerce.domain.item.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class ImageNotFound extends GlobalException {

    private static final String MESSAGE = "이미지를 찾을 수 없습니다.";

    public ImageNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
