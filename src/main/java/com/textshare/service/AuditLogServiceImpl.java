package com.textshare.service;

import com.textshare.entity.AccessLog;
import com.textshare.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AccessLogRepository accessLogRepository;

    @Override
    @Async
    public void logAccess(String textId, String ip, String userAgent, String action) {
        AccessLog accessLog = AccessLog.builder()
                .textId(textId)
                .ipAddress(ip)
                .userAgent(userAgent)
                .action(action)
                .build();
        accessLogRepository.save(accessLog);
        log.debug("Logged access: textId={}, ip={}, action={}", textId, ip, action);
    }
}
