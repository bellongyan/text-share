package com.textshare.service;

import com.textshare.dto.request.CreateTextRequest;
import com.textshare.dto.response.TextCreateResponse;
import com.textshare.dto.response.TextGetResponse;
import com.textshare.entity.Text;
import com.textshare.exception.ForbiddenException;
import com.textshare.exception.TextNotFoundException;
import com.textshare.repository.TextRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {

    private final TextRepository textRepository;
    private final SecurityService securityService;
    private final RateLimitService rateLimitService;
    private final AuditLogService auditLogService;
    private final StringRedisTemplate redisTemplate;

    @Value("${textshare.text.default-expire-hours:24}")
    private int defaultExpireHours;

    @Value("${textshare.text.max-length:50000}")
    private int maxLength;

    private static final String VIEW_COUNT_KEY_PREFIX = "text:views:";
    private static final String CONTENT_CACHE_KEY_PREFIX = "text:content:";

    @Override
    @Transactional
    public TextCreateResponse createText(CreateTextRequest request, String ip, String userAgent) {
        if (request.getContent() != null && request.getContent().length() > maxLength) {
            throw new com.textshare.exception.ValidationException(
                    "textshare.error.validation.content.size");
        }
        String id = securityService.generateObfuscatedId();

        Text text = Text.builder()
                .id(id)
                .content(request.getContent())
                .ipAddress(ip)
                .userAgent(userAgent)
                .deviceInfo(request.getDevice())
                .expiresAt(LocalDateTime.now().plusHours(defaultExpireHours))
                .build();

        textRepository.save(text);

        String link = "/s/" + id;
        log.info("Created text share: id={}, ip={}", id, ip);

        return TextCreateResponse.builder()
                .id(id)
                .link(link)
                .expiresAt(text.getExpiresAt())
                .viewCount(0)
                .build();
    }

    @Override
    public TextGetResponse getText(String id) {
        Text text = textRepository.findById(id)
                .filter(t -> !t.getIsDeleted())
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(TextNotFoundException::new);

        String cacheKey = CONTENT_CACHE_KEY_PREFIX + id;
        redisTemplate.opsForValue().set(cacheKey, text.getContent(), 1, TimeUnit.HOURS);

        return TextGetResponse.builder()
                .id(text.getId())
                .content(securityService.escapeHtml(text.getContent()))
                .device(text.getDeviceInfo())
                .viewCount(text.getViewCount())
                .createdAt(text.getCreatedAt())
                .expiresAt(text.getExpiresAt())
                .isExpired(text.getExpiresAt().isBefore(LocalDateTime.now()))
                .build();
    }

    @Override
    public int incrementViewCount(String id) {
        String viewKey = VIEW_COUNT_KEY_PREFIX + id;
        Long count = redisTemplate.opsForValue().increment(viewKey);
        // DB sync is handled by CleanupScheduler.syncViewCount() to avoid race conditions
        return count != null ? count.intValue() : 0;
    }

    @Override
    public int getViewCount(String id) {
        String viewKey = VIEW_COUNT_KEY_PREFIX + id;
        String countStr = redisTemplate.opsForValue().get(viewKey);
        return countStr != null ? Integer.parseInt(countStr) : 0;
    }

    @Override
    @Transactional
    public boolean deleteText(String id, String ip) {
        Text text = textRepository.findById(id)
                .filter(t -> !t.getIsDeleted())
                .orElseThrow(TextNotFoundException::new);

        if (!text.getIpAddress().equals(ip)) {
            throw new ForbiddenException();
        }

        text.setIsDeleted(true);
        textRepository.save(text);

        auditLogService.logAccess(id, ip, null, "DELETE");
        log.info("Deleted text share: id={}, ip={}", id, ip);

        return true;
    }
}
