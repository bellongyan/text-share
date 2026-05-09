package com.textshare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextGetResponse {

    private String id;
    private String content;
    private String device;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean isExpired;
}
