package com.textshare.service;

import com.textshare.dto.request.CreateTextRequest;
import com.textshare.dto.response.TextCreateResponse;
import com.textshare.dto.response.TextGetResponse;

public interface TextService {

    TextCreateResponse createText(CreateTextRequest request, String ip, String userAgent);

    TextGetResponse getText(String id);

    int incrementViewCount(String id);

    int getViewCount(String id);

    boolean deleteText(String id, String ip);
}
