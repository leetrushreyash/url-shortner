package com.application.url_shortner.exception;

public class UrlNotFoundException extends RuntimeException{
    public UrlNotFoundException(String msg){
        super(msg);
    }
}
