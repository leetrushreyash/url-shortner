package com.application.url_shortner.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.url_shortner.dto.UrlCountResponse;
import com.application.url_shortner.dto.UrlRequest;
import com.application.url_shortner.service.UrlService;

@RestController
@RequestMapping("/api/url")
public class UrlController {
    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> getShortenUrl(@RequestBody UrlRequest request) {
        return ResponseEntity.ok(urlService.shortenUrl(request));
    }

    @GetMapping("/{shortencode}")
    public ResponseEntity<Void> redirectUser(@PathVariable String shortencode) {
        String originalUrl = urlService.getOriginalUrl(shortencode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }

    @GetMapping("/{shortencode}/count")
    public UrlCountResponse getUrlCount(@PathVariable String shortencode) {
        return urlService.getClickCount(shortencode);
    }
}
