package com.textshare.scheduler;

import com.textshare.repository.TextRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        List<String> keysToSync = new ArrayList<>();
        try (Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
                .match(VIEW_COUNT_KEY_PREFIX + "*")
                .count(100)
                .build())) {
            while (cursor.hasNext()) {
                keysToSync.add(cursor.next());
            }
        }

        if (keysToSync.isEmpty()) {
            return;
        }

        for (String key : keysToSync) {
            String textId = key.replace(VIEW_COUNT_KEY_PREFIX, "");
            String countStr = redisTemplate.opsForValue().get(key);
            if (countStr != null) {
                int count = Integer.parseInt(countStr);
                textRepository.updateViewCount(textId, count);
            }
        }
        log.debug("同步浏览量到数据库: {} 条", keysToSync.size());
    }
}
