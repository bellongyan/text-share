package com.textshare.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-Code";
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantCode = request.getHeader(TENANT_HEADER);
        if (tenantCode != null && !tenantCode.isBlank()) {
            CURRENT_TENANT.set(tenantCode);
            log.debug("Tenant context set: {}", tenantCode);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            // Clean up tenant context if it was set by this request
            String tenantCode = request.getHeader(TENANT_HEADER);
            if (tenantCode != null && !tenantCode.isBlank()) {
                CURRENT_TENANT.remove();
            }
        } catch (Exception e) {
            // Ensure ThreadLocal is always cleaned, even if cleanup fails
            try {
                CURRENT_TENANT.remove();
            } catch (Exception ignored) {
                // ignore
            }
        }
    }
}
