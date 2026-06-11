package com.application.url_shortner;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.application.url_shortner.repository.UrlRepository;

@Component
public class UrlCleanupScheduler {
    private UrlRepository urlRepository;

    @Autowired
    public UrlCleanupScheduler(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredUrls() {
        System.out.println("Running background cleanup task at: " + LocalDateTime.now());
        urlRepository.deleteExpiredUrls(LocalDateTime.now());
    }
}
