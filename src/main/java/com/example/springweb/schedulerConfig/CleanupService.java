package com.example.springweb.schedulerConfig;

import com.example.springweb.entity.PasswordResetToken;
import com.example.springweb.repository.PasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CleanupService {

    private final PasswordTokenRepository passwordTokenRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpiredRecords() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<PasswordResetToken> expiredTokens = passwordTokenRepository.findAllByExpiryDateBefore(oneHourAgo);
        if (!expiredTokens.isEmpty()) {
            passwordTokenRepository.deleteAll(expiredTokens);
        }
    }
}

