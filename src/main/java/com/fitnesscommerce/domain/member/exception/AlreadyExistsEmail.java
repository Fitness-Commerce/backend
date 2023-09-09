package com.fitnesscommerce.domain.member.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class AlreadyExistsEmail extends GlobalException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public AlreadyExistsEmail() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
