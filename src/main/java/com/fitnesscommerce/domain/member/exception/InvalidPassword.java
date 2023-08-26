package com.fitnesscommerce.domain.member.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class InvalidPassword extends GlobalException {

    private static final String MESSAGE = "비밀번호가 틀립니다.";

    public InvalidPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
