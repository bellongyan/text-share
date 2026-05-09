package com.textshare.controller;

import com.textshare.dto.request.CreateTextRequest;
import com.textshare.dto.response.*;
import com.textshare.exception.RateLimitExceededException;
import com.textshare.service.AuditLogService;
import com.textshare.service.RateLimitService;
import com.textshare.service.TextService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/texts")
@RequiredArgsConstructor
public class TextController {

    private final TextService textService;
    private final RateLimitService rateLimitService;
    private final AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<ApiResponse<TextCreateResponse>> createText(
            @Valid @RequestBody CreateTextRequest request,
            HttpServletRequest httpRequest) {

        String ip = getClientIp(httpRequest);
        if (!rateLimitService.isAllowed(ip)) {
            throw new RateLimitExceededException("请求过于频繁，请稍后再试");
        }

        String userAgent = httpRequest.getHeader("User-Agent");
        TextCreateResponse response = textService.createText(request, ip, userAgent);

        auditLogService.logAccess(response.getId(), ip, userAgent, "CREATE");

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TextGetResponse>> getText(
            @PathVariable String id,
            HttpServletRequest httpRequest) {

        String ip = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        TextGetResponse response = textService.getText(id);
        auditLogService.logAccess(id, ip, userAgent, "VIEW");

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<ApiResponse<ViewCountResponse>> incrementView(
            @PathVariable String id,
            HttpServletRequest httpRequest) {

        int viewCount = textService.incrementViewCount(id);

        String ip = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        auditLogService.logAccess(id, ip, userAgent, "VIEW");

        return ResponseEntity.ok(ApiResponse.success(new ViewCountResponse(viewCount)));
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<ApiResponse<ViewCountResponse>> getViewCount(
            @PathVariable String id) {
        int viewCount = textService.getViewCount(id);
        return ResponseEntity.ok(ApiResponse.success(new ViewCountResponse(viewCount)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteText(
            @PathVariable String id,
            HttpServletRequest httpRequest) {

        String ip = getClientIp(httpRequest);
        textService.deleteText(id, ip);

        return ResponseEntity.ok(ApiResponse.success("删除成功"));
    }

    private String getClientIp(HttpServletRequest request) {
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
