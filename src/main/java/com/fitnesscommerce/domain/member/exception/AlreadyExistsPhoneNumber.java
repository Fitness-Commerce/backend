package com.fitnesscommerce.domain.member.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class AlreadyExistsPhoneNumber extends GlobalException {

    private static final String MESSAGE = "이미 존재하는 번호입니다.";

    public AlreadyExistsPhoneNumber() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
