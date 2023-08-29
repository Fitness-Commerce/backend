package com.fitnesscommerce.domain.item.exception;

import com.fitnesscommerce.global.exception.GlobalException;

public class ItemCategoryNotFound extends GlobalException {

    private static final String MESSAGE = "해당 카테고리를 찾을 수 없습니다.";

    public ItemCategoryNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
