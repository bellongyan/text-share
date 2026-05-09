package com.textshare.service;

import com.textshare.dto.request.CreateTextRequest;
import com.textshare.dto.response.TextCreateResponse;
import com.textshare.dto.response.TextGetResponse;
import com.textshare.entity.Text;
import com.textshare.exception.TextNotFoundException;
import com.textshare.repository.TextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextServiceImplTest {

    @Mock
    private TextRepository textRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private RateLimitService rateLimitService;

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private TextServiceImpl textService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(textService, "defaultExpireHours", 24);
        ReflectionTestUtils.setField(textService, "maxLength", 50000);
    }

    @Test
    void createText_ShouldCreateTextSuccessfully() {
        CreateTextRequest request = CreateTextRequest.builder()
                .content("Test content")
                .device("Test device")
                .build();

        when(securityService.escapeHtml(any())).thenReturn("Test content");
        when(securityService.generateObfuscatedId()).thenReturn("test-id-123");
        when(textRepository.save(any(Text.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TextCreateResponse response = textService.createText(request, "127.0.0.1", "Test-Agent");

        assertNotNull(response);
        assertEquals("test-id-123", response.getId());
        assertEquals("/s/test-id-123", response.getLink());
        assertEquals(0, response.getViewCount());
        verify(textRepository).save(any(Text.class));
    }

    @Test
    void getText_ShouldReturnText_WhenExists() {
        Text text = Text.builder()
                .id("test-id")
                .content("Test content")
                .deviceInfo("Test device")
                .viewCount(5)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();

        when(textRepository.findById("test-id")).thenReturn(Optional.of(text));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        TextGetResponse response = textService.getText("test-id");

        assertNotNull(response);
        assertEquals("test-id", response.getId());
        assertEquals("Test content", response.getContent());
        assertEquals(5, response.getViewCount());
    }

    @Test
    void getText_ShouldThrowException_WhenNotFound() {
        when(textRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(TextNotFoundException.class, () -> textService.getText("invalid-id"));
    }

    @Test
    void getText_ShouldThrowException_WhenExpired() {
        Text text = Text.builder()
                .id("test-id")
                .content("Test content")
                .expiresAt(LocalDateTime.now().minusHours(1))
                .isDeleted(false)
                .build();

        when(textRepository.findById("test-id")).thenReturn(Optional.of(text));

        assertThrows(TextNotFoundException.class, () -> textService.getText("test-id"));
    }

    @Test
    void deleteText_ShouldDelete_WhenOwner() {
        Text text = Text.builder()
                .id("test-id")
                .ipAddress("127.0.0.1")
                .isDeleted(false)
                .build();

        when(textRepository.findById("test-id")).thenReturn(Optional.of(text));
        when(textRepository.save(any(Text.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = textService.deleteText("test-id", "127.0.0.1");

        assertTrue(result);
        verify(textRepository).save(argThat(t -> t.getIsDeleted()));
    }
}
