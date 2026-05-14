package com.textshare.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        // HTML escaping is done at output time in the frontend
        // to preserve the original data and avoid double-escaping issues
        return content;
    }

    @Override
    public boolean isIpAllowed(String ip) {
        return true;
    }
}
