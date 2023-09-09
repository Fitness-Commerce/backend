package com.fitnesscommerce.global.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GlobalException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();

    public GlobalException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}
