package com.textshare.service;

public interface SecurityService {

    String generateObfuscatedId();

    String escapeHtml(String content);

    boolean isIpAllowed(String ip);
}
