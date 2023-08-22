package com.fitnesscommerce.domain.member.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class IdNotFound extends GlobalException {

    private static final String MESSAGE = "아이디를 찾을 수 없습니다.";
    public IdNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
