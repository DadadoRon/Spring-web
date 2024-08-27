package com.example.springweb.repository;

import com.example.springweb.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    Optional<PasswordResetToken> findByToken(String token);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.expiryDate < :expiryDate")
    List<PasswordResetToken> findAllByExpiryDateBefore(@Param("expiryDate") LocalDateTime expiryDate);
}
