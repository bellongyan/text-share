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

    @NotBlank(message = "文本内容不能为空")
    @Size(max = 50000, message = "文本内容不能超过50000字符")
    private String content;

    private String device;
}
