package com.github.twitch4j.eventsub.util;

import com.github.twitch4j.common.util.CryptoUtils;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.api.domain.ExpiryType;
import io.github.xanthic.cache.core.CacheApi;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@Slf4j
@UtilityClass
public class EventSubVerifier {

    /**
     * Twitch recommends a buffer of 10 minutes for resiliency against replay attacks
     *
     * @see <a href="https://dev.twitch.tv/docs/eventsub">Official docs</a>
     */
    private final Duration RECENT_EVENT = Duration.ofMinutes(10L);

    /**
     * The Twitch-Eventsub-Message-Id's that have been observed during {@link #RECENT_EVENT}
     */
    private final Cache<String, Boolean> RECENT_MESSAGE_IDS = CacheApi.create(spec -> {
        spec.expiryType(ExpiryType.POST_WRITE);
        spec.expiryTime(RECENT_EVENT);
        spec.maxSize(65_536L);
    });

    /**
     * Twitch's prefix for Twitch-Eventsub-Message-Signature
     */
    private final String SIGNATURE_HASH_PREFIX = "sha256=";

    /**
     * Java algorithm name that corresponds to {@link #SIGNATURE_HASH_PREFIX}
     */
    public final String JAVA_HMAC_ALGORITHM = "HmacSHA256";

    /**
     * The number of characters in hashes produced by {@link #JAVA_HMAC_ALGORITHM}
     */
    private final int HASH_LENGTH = 256 / 4;

    /**
     * A thread-local {@link Mac} instance of {@link #JAVA_HMAC_ALGORITHM}
     */
    private final ThreadLocal<Mac> HMAC_FUNCTION = ThreadLocal.withInitial(() -> {
        try {
            return Mac.getInstance(JAVA_HMAC_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    });

    /**
     * @param messageId Twitch-Eventsub-Message-Id
     * @return whether the message id has not been observed recently
     */
    public boolean verifyMessageId(String messageId) {
        return messageId != null && !messageId.isEmpty() && RECENT_MESSAGE_IDS.putIfAbsent(messageId, Boolean.TRUE) == null;
    }

    /**
     * @param messageTimestamp Twitch-Eventsub-Message-Timestamp
     * @return whether the event occurred recently
     */
    public boolean verifyTimestamp(String messageTimestamp) {
        if (messageTimestamp == null || messageTimestamp.isEmpty())
            return false;

        final Instant timestamp;
        try {
            timestamp = Instant.parse(messageTimestamp);
        } catch (DateTimeParseException e) {
            log.debug("Failed to parse timestamp: " + messageTimestamp, e);
            return false;
        }

        return Duration.between(timestamp, Instant.now()).compareTo(RECENT_EVENT) < 0;
    }

    /**
     * @param secret            The subscription secret.
     * @param messageId         Twitch-Eventsub-Message-Id
     * @param messageTimestamp  Twitch-Eventsub-Message-Timestamp
     * @param requestBody       The raw bytes of the request body.
     * @param expectedSignature Twitch-Eventsub-Message-Signature
     * @return whether the signature indicates an authentic event
     */
    public boolean verifySignature(SecretKeySpec secret, String messageId, String messageTimestamp, byte[] requestBody, String expectedSignature) {
        if (secret == null || expectedSignature == null || messageId == null || messageTimestamp == null || requestBody == null) {
            log.warn("Could not verify eventsub signature with null argument");
            return false;
        }

        if (expectedSignature.length() - SIGNATURE_HASH_PREFIX.length() != HASH_LENGTH || !StringUtils.startsWithIgnoreCase(expectedSignature, SIGNATURE_HASH_PREFIX)) {
            log.debug("Could not verify unknown eventsub signature hash scheme; " + expectedSignature);
            return false;
        }

        final Mac mac = HMAC_FUNCTION.get();
        if (mac == null) {
            log.error("Unable to prepare hash function for eventsub signature verification!");
            return false;
        }

        try {
            mac.init(secret);
        } catch (InvalidKeyException e) {
            log.error("Unable to initialize secret for eventsub signature verification!", e);
            return false;
        }

        final byte[] id = messageId.getBytes(StandardCharsets.UTF_8);
        final byte[] timestamp = messageTimestamp.getBytes(StandardCharsets.UTF_8);
        final byte[] message = new byte[id.length + timestamp.length + requestBody.length];
        System.arraycopy(id, 0, message, 0, id.length);
        System.arraycopy(timestamp, 0, message, id.length, timestamp.length);
        System.arraycopy(requestBody, 0, message, id.length + timestamp.length, requestBody.length);
        final byte[] computedHmac = mac.doFinal(message);
        mac.reset(); // Clean-up
        final byte[] expectedHmac = CryptoUtils.hexToBytes(expectedSignature.substring(SIGNATURE_HASH_PREFIX.length()));
        return MessageDigest.isEqual(computedHmac, expectedHmac); // constant-time comparison
    }

    /**
     * @see #verifySignature(SecretKeySpec, String, String, byte[], String)
     */
    public boolean verifySignature(byte[] secret, String messageId, String messageTimestamp, byte[] requestBody, String expectedSignature) {
        return verifySignature(new SecretKeySpec(secret, JAVA_HMAC_ALGORITHM), messageId, messageTimestamp, requestBody, expectedSignature);
    }

    /**
     * @see #verifySignature(SecretKeySpec, String, String, byte[], String)
     */
    public boolean verifySignature(String secret, String messageId, String messageTimestamp, byte[] requestBody, String expectedSignature) {
        return verifySignature(secret.getBytes(StandardCharsets.UTF_8), messageId, messageTimestamp, requestBody, expectedSignature);
    }

}
