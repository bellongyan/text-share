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
public class TextCreateResponse {

    private String id;
    private String link;
    private LocalDateTime expiresAt;
    private Integer viewCount;
}
