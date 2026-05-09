package com.textshare.util;

import java.security.SecureRandom;
import java.util.Base64;

public final class IdGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Base64.Encoder BASE64_URL = Base64.getUrlEncoder().withoutPadding();

    private IdGenerator() {
    }

    public static String generate() {
        byte[] bytes = new byte[16];
        SECURE_RANDOM.nextBytes(bytes);
        return BASE64_URL.encodeToString(bytes);
    }

    public static String generate(int length) {
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return BASE64_URL.encodeToString(bytes).substring(0, length);
    }
}
