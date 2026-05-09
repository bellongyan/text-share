package com.textshare.scheduler;

import com.textshare.repository.TextRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanupScheduler {

    private final TextRepository textRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String VIEW_COUNT_KEY_PREFIX = "text:views:";

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void cleanupExpiredTexts() {
        int deleted = textRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        log.info("清理过期文本: {} 条", deleted);
    }

    @Scheduled(fixedRate = 300000)
    public void syncViewCount() {
        Set<String> keys = redisTemplate.keys(VIEW_COUNT_KEY_PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            String textId = key.replace(VIEW_COUNT_KEY_PREFIX, "");
            String countStr = redisTemplate.opsForValue().get(key);
            if (countStr != null) {
                int count = Integer.parseInt(countStr);
                textRepository.updateViewCount(textId, count);
            }
        }
        log.debug("同步浏览量到数据库: {} 条", keys.size());
    }
}
