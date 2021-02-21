package com.khpi.farmacy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StrategyNotFoundException extends RuntimeException {

    public StrategyNotFoundException(String message) {
        super(message);
    }
}
