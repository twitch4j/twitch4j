package com.github.twitch4j.pubsub;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.test.TestEventManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.regex.Pattern;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.times;

@Slf4j
@Tag("unittest")
public class TwitchPubSubTest {
    private WebsocketConnection connection;

    private TestEventManager eventManager;

    private TwitchPubSub pubSub;

    private OAuth2Credential credential = new OAuth2Credential("twitch", "my-secret-token");

    @BeforeEach
    void init() {
        // mock connection
        connection = Mockito.mock(WebsocketConnection.class);

        // mock eventManager
        eventManager = new TestEventManager();

        // construct twitchChat
        pubSub = TwitchPubSubBuilder.builder()
            .withWebsocketConnection(connection)
            .withEventManager(eventManager)
            .build();
    }

    @Test
    public void testTopicListen() {
        // fake connected state
        Mockito.when(connection.getConnectionState()).thenReturn(WebsocketConnectionState.CONNECTED);

        // join channel
        pubSub.listenForCheerEvents(credential, "149223493");
        pubSub.listenForSubscriptionEvents(credential, "149223493");
        pubSub.listenForWhisperEvents(credential, "149223493");

        // wait for all commands to be processed
        await().atMost(Duration.ofSeconds(1)).until(() -> pubSub.commandQueue.size() == 0);

        // verify
        Mockito.verify(connection, times(1)).sendText(matches(noncePattern("{\"type\":\"LISTEN\",\"nonce\":\"NONCE_ANY\",\"data\":{\"auth_token\":\"my-secret-token\",\"topics\":[\"channel-bits-events-v2.149223493\"]}}")));
        Mockito.verify(connection, times(1)).sendText(matches(noncePattern("{\"type\":\"LISTEN\",\"nonce\":\"NONCE_ANY\",\"data\":{\"auth_token\":\"my-secret-token\",\"topics\":[\"channel-subscribe-events-v1.149223493\"]}}")));
        Mockito.verify(connection, times(1)).sendText(matches(noncePattern("{\"type\":\"LISTEN\",\"nonce\":\"NONCE_ANY\",\"data\":{\"auth_token\":\"my-secret-token\",\"topics\":[\"whispers.149223493\"]}}")));
    }

    private String noncePattern(String input) {
        return Pattern.quote(input).replace("NONCE_ANY", "\\E.+\\Q");
    }
}
