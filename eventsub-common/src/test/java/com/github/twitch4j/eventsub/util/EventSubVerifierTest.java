package com.github.twitch4j.eventsub.util;

import com.github.twitch4j.common.util.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("unittest")
public class EventSubVerifierTest {

    @Test
    @DisplayName("Ensure only new message id's are valid")
    public void validateId() {
        UUID event1 = UUID.randomUUID();
        assertTrue(EventSubVerifier.verifyMessageId(event1.toString()));
        assertFalse(EventSubVerifier.verifyMessageId(event1.toString()));

        UUID event2 = UUID.randomUUID();
        assertTrue(EventSubVerifier.verifyMessageId(event2.toString()));
        assertFalse(EventSubVerifier.verifyMessageId(event2.toString()));
    }

    @Test
    @DisplayName("Ensure only recent events are valid")
    public void validateTime() {
        assertTrue(EventSubVerifier.verifyTimestamp(Instant.now().toString()));
        assertTrue(EventSubVerifier.verifyTimestamp(Instant.now().minus(1, ChronoUnit.MINUTES).toString()));
        assertTrue(EventSubVerifier.verifyTimestamp(Instant.now().plus(1, ChronoUnit.MINUTES).toString()));
        assertFalse(EventSubVerifier.verifyTimestamp(Instant.now().minus(11, ChronoUnit.MINUTES).toString()));
    }

    @Test
    @DisplayName("Ensure signature validation is functioning")
    public void validateSignature() {
        final String secret = "Twitch4J",
            id = "7d4c39a4-10b3-4166-b1fb-56130d1f693b",
            time = "2020-12-13T14:15:16.1718Z",
            expect = "sha256=2ac8d26bf60837663d8969f3cb4f22f685c8fa0a4b99d533c8bf622ed4596cc6";
        final byte[] body = ("{\"hello\":\"world\"}").getBytes(StandardCharsets.UTF_8);

        assertTrue(EventSubVerifier.verifySignature(secret, id, time, body, expect));
        assertTrue(EventSubVerifier.verifySignature(secret, id, time, body, expect.toUpperCase()));
        assertFalse(EventSubVerifier.verifySignature("s3cRe7", id, time, body, expect));
        assertFalse(EventSubVerifier.verifySignature(secret, UUID.randomUUID().toString(), time, body, expect));
        assertFalse(EventSubVerifier.verifySignature(secret, id, Instant.now().toString(), body, expect));
        assertFalse(EventSubVerifier.verifySignature(secret, id, time, null, expect));
        assertFalse(EventSubVerifier.verifySignature(secret, id, time, "Hello World".getBytes(StandardCharsets.UTF_8), expect));
        assertFalse(EventSubVerifier.verifySignature(secret, id, time, body, "sha256=f56bf6ce06a1adf46fa27831d7d15d"));
        assertFalse(EventSubVerifier.verifySignature(secret, id, time, body, "sha256=" + CryptoUtils.generateNonce(64)));
        assertFalse(EventSubVerifier.verifySignature(secret, id, time, body, "sha256=" + CryptoUtils.generateNonce(60) + "zÉñü"));
    }

}
