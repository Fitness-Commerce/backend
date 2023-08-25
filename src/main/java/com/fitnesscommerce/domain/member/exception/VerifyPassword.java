package com.fitnesscommerce.domain.member.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class VerifyPassword extends GlobalException {

    private static final String MESSAGE = "비밀번호를 다시 확인해주세요.";

    public VerifyPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
