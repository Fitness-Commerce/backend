package com.fitnesscommerce.domain.auth.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class InvalidRefreshToken extends GlobalException {

    private static final String MESSAGE = "다시 로그인 해주세요.";

    public InvalidRefreshToken() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
