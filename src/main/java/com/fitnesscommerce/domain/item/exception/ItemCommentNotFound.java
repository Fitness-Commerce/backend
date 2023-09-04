package com.fitnesscommerce.domain.item.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class ItemCommentNotFound extends GlobalException {

    private static final String MESSAGE = "해당 댓글을 찾을 수 없습니다.";

    public ItemCommentNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
