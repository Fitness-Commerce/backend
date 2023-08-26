package com.fitnesscommerce.domain.member.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class EmailNotFound extends GlobalException {

    private static final String MESSAGE = "해당 이메일을 찾을 수 없습니다.";

    public EmailNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
