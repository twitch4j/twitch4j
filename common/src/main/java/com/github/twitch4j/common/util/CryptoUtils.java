package com.github.twitch4j.common.util;

import java.security.SecureRandom;
import java.util.Random;

public class CryptoUtils {
    private static final ThreadLocal<Random> RANDOM;
    private static final char[] CHARSET;

    public static String generateNonce(final int length) {
        final Random rand = RANDOM.get();
        final StringBuilder sb = new StringBuilder(length);

        while (sb.length() < length) {
            sb.append(CHARSET[rand.nextInt(CHARSET.length)]);
        }

        return sb.toString();
    }

    static {
        RANDOM = ThreadLocal.withInitial(SecureRandom::new);
        CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    }
}
