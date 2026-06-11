package com.application.url_shortner.dto;

public class UrlRequest {
    private String url;
    private String customCode;
    private Long ttlInSeconds;

    public Long getTtlInSeconds() {
        return ttlInSeconds;
    }

    public void setTtlInSeconds(Long ttlInSeconds) {
        this.ttlInSeconds = ttlInSeconds;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}