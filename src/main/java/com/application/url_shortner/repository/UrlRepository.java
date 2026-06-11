package com.application.url_shortner.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.url_shortner.entity.UrlMapping;

import jakarta.transaction.Transactional;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    @Query("UPDATE UrlMapping u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :shortCode")
    @Modifying
    @Transactional
    void updateClickCount(@Param("shortCode") String shortCode);

    @Query("SELECT u.clickCount FROM UrlMapping u WHERE u.shortCode = :shortCode")
    Optional<Long> getClickCount(@Param("shortCode") String shortCode);

    @Modifying
    @Transactional
    @Query("DELETE FROM UrlMapping u where u.expiresAt < :now")
    void deleteExpiredUrls(@Param("now") LocalDateTime now);

}
