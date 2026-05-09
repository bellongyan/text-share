package com.textshare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final StringRedisTemplate redisTemplate;

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";
    private static final int QPS = 20;
    private static final int WINDOW_SECONDS = 1;

    private static final String LUA_SCRIPT = """
            local key = KEYS[1]
            local limit = tonumber(ARGV[1])
            local window = tonumber(ARGV[2])
            local current = redis.call('GET', key)
            if current and tonumber(current) >= limit then
                return 0
            end
            current = redis.call('INCR', key)
            if current == 1 then
                redis.call('EXPIRE', key, window)
            end
            return 1
            """;

    @Override
    public boolean isAllowed(String ip) {
        String key = RATE_LIMIT_KEY_PREFIX + ip;
        RedisScript<Long> script = RedisScript.of(LUA_SCRIPT, Long.class);
        Long result = redisTemplate.execute(script, Collections.singletonList(key), String.valueOf(QPS), String.valueOf(WINDOW_SECONDS));
        return result != null && result == 1;
    }

    @Override
    public int getRemainingRequests(String ip) {
        String key = RATE_LIMIT_KEY_PREFIX + ip;
        String current = redisTemplate.opsForValue().get(key);
        if (current == null) {
            return QPS;
        }
        return Math.max(0, QPS - Integer.parseInt(current));
    }

    @Override
    public void resetLimit(String ip) {
        String key = RATE_LIMIT_KEY_PREFIX + ip;
        redisTemplate.delete(key);
    }
}
