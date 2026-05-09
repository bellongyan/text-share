package com.textshare.service;

public interface RateLimitService {

    boolean isAllowed(String ip);

    int getRemainingRequests(String ip);

    void resetLimit(String ip);
}
