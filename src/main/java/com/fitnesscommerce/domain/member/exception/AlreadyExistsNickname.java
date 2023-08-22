package com.fitnesscommerce.domain.member.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class AlreadyExistsNickname extends GlobalException {

    private static final String MESSAGE = "이미 존재하는 닉네임입니다.";

    public AlreadyExistsNickname() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
