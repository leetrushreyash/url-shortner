package com.application.url_shortner.service;

import java.net.URI;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.url_shortner.dto.UrlCountResponse;
import com.application.url_shortner.dto.UrlRequest;
import com.application.url_shortner.entity.UrlMapping;
import com.application.url_shortner.exception.CustomCodeAlreadyExistsException;
import com.application.url_shortner.exception.UrlNotFoundException;
import com.application.url_shortner.repository.UrlRepository;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final static int MAX_ITR = 10000;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final Random random = new SecureRandom();

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(UrlRequest request) {
        int i = 0;
        String longUrl = request.getUrl();
        validateUrl(longUrl);
        if (request.getCustomCode() != null && !request.getCustomCode().trim().isEmpty()) {
            handleCustomCode(longUrl, request.getCustomCode(), request.getTtlInSeconds());
            return request.getCustomCode();
        }
        while (i < MAX_ITR) {
            String shortenString = generateRandomCode();
            if (!urlRepository.existsByShortCode(shortenString)) {
                UrlMapping mapping = new UrlMapping();
                mapping.setLongUrl(longUrl);
                mapping.setShortCode(shortenString);
                mapping.setExpiresAt(calculateExpiry(request.getTtlInSeconds()));
                urlRepository.save(mapping);

                return shortenString;
            }
            i++;
        }
        throw new RuntimeException("System was unable to generate a unique code after " + MAX_ITR + " attempts.");
    }

    public void handleCustomCode(String longUrl, String customCode, Long ttlInSeconds) {
        if (!customCode.matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new IllegalArgumentException("Illegal Custom URL");
        }
        if (urlRepository.existsByShortCode(customCode))
            throw new CustomCodeAlreadyExistsException("This url already exists");
        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl(longUrl);
        mapping.setShortCode(customCode);
        mapping.setExpiresAt(calculateExpiry(ttlInSeconds));
        urlRepository.save(mapping);
    }

    public String getOriginalUrl(String shortUrl) {

        UrlMapping mapping = urlRepository.findByShortCode(shortUrl).orElseThrow(
                () -> new UrlNotFoundException("url does not exists"));

        if (mapping.getExpiresAt() != null && mapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException("url does not exists");
        }
        urlRepository.updateClickCount(shortUrl);
        return mapping.getLongUrl();
    }

    public UrlCountResponse getClickCount(String shortUrl) {
        UrlMapping mapping = urlRepository.findByShortCode(shortUrl).orElseThrow(
                () -> new UrlNotFoundException("url not found"));

        UrlCountResponse response = new UrlCountResponse();
        response.setCount(mapping.getClickCount());
        response.setUrl(shortUrl);
        return response;
    }

    @SuppressWarnings("UseSpecificCatch")
    private void validateUrl(String url) {
        try {
            URI.create(url).toURL();
        } catch (Exception e) {
            throw new IllegalArgumentException("The Provided URL is malformed or invalid");
        }
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(7);
        for (int i = 0; i < 7; i++) {
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }

    private LocalDateTime calculateExpiry(Long ttlInSeconds) {
        long ttl = (ttlInSeconds != null) ? ttlInSeconds : (30L * 24 * 60 * 60); // 30 days default
        return LocalDateTime.now().plusSeconds(ttl);
    }

}
