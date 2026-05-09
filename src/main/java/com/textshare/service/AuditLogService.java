package com.textshare.service;

import com.textshare.entity.AccessLog;

public interface AuditLogService {

    void logAccess(String textId, String ip, String userAgent, String action);
}
