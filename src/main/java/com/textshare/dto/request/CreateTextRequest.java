package com.textshare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTextRequest {

    @NotBlank(message = "textshare.error.validation.content.blank")
    @Size(max = 50000, message = "textshare.error.validation.content.size")
    private String content;

    private String device;
}
