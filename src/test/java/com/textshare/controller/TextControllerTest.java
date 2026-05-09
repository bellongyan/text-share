package com.textshare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.textshare.dto.request.CreateTextRequest;
import com.textshare.dto.response.TextCreateResponse;
import com.textshare.dto.response.TextGetResponse;
import com.textshare.exception.TextNotFoundException;
import com.textshare.service.AuditLogService;
import com.textshare.service.RateLimitService;
import com.textshare.service.TextService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TextController.class)
class TextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TextService textService;

    @MockBean
    private RateLimitService rateLimitService;

    @MockBean
    private AuditLogService auditLogService;

    @Test
    void createText_ShouldReturn201_WhenValid() throws Exception {
        CreateTextRequest request = CreateTextRequest.builder()
                .content("Test content")
                .build();

        TextCreateResponse response = TextCreateResponse.builder()
                .id("test-id")
                .link("/s/test-id")
                .expiresAt(LocalDateTime.now().plusHours(24))
                .viewCount(0)
                .build();

        when(rateLimitService.isAllowed(anyString())).thenReturn(true);
        when(textService.createText(any(), anyString(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/texts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("test-id"));
    }

    @Test
    void createText_ShouldReturn400_WhenContentEmpty() throws Exception {
        CreateTextRequest request = CreateTextRequest.builder()
                .content("")
                .build();

        mockMvc.perform(post("/api/v1/texts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createText_ShouldReturn429_WhenRateLimited() throws Exception {
        CreateTextRequest request = CreateTextRequest.builder()
                .content("Test content")
                .build();

        when(rateLimitService.isAllowed(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/v1/texts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void getText_ShouldReturn200_WhenExists() throws Exception {
        TextGetResponse response = TextGetResponse.builder()
                .id("test-id")
                .content("Test content")
                .viewCount(5)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .isExpired(false)
                .build();

        when(textService.getText("test-id")).thenReturn(response);

        mockMvc.perform(get("/api/v1/texts/test-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("test-id"));
    }

    @Test
    void getText_ShouldReturn404_WhenNotFound() throws Exception {
        when(textService.getText("invalid-id")).thenThrow(new TextNotFoundException("该分享不存在或已过期"));

        mockMvc.perform(get("/api/v1/texts/invalid-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("TEXT_NOT_FOUND"));
    }

    @Test
    void incrementView_ShouldReturn200() throws Exception {
        when(textService.incrementViewCount("test-id")).thenReturn(6);

        mockMvc.perform(post("/api/v1/texts/test-id/view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.viewCount").value(6));
    }

    @Test
    void deleteText_ShouldReturn200_WhenOwner() throws Exception {
        when(textService.deleteText("test-id", "127.0.0.1")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/texts/test-id")
                        .header("X-Real-IP", "127.0.0.1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
