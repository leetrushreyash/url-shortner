package com.application.url_shortner.exception;

public class CustomCodeAlreadyExistsException extends RuntimeException {

    public CustomCodeAlreadyExistsException(String msg) {
        super(msg);
    }

}
