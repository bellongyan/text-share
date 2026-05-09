package com.textshare.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.textshare.util.HtmlEscapeUtil;
import com.textshare.util.IdGenerator;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    @Override
    public String generateObfuscatedId() {
        return IdGenerator.generate();
    }

    @Override
    public String escapeHtml(String content) {
        return HtmlEscapeUtil.escape(content);
    }

    @Override
    public boolean isIpAllowed(String ip) {
        return true;
    }
}
