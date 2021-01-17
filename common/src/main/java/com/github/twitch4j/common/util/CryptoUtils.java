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

    public static byte[] hexToBytes(String hex) {
        final int n = hex.length() / 2;
        final byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++) {
            final char a = hex.charAt(i * 2);
            final char b = hex.charAt(i * 2 + 1);
            bytes[i] = (byte) ((Character.digit(a, 16) << 4) + (Character.digit(b, 16)));
        }
        return bytes;
    }

    static {
        RANDOM = ThreadLocal.withInitial(SecureRandom::new);
        CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    }
}
