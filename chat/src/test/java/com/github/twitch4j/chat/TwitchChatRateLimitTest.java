package com.github.twitch4j.chat;

import com.github.twitch4j.chat.util.TestUtils;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.times;

@Slf4j
public class TwitchChatRateLimitTest {

    private WebsocketConnection connection;

    private TwitchChat twitchChat;

    @BeforeEach
    void init() {
        // mock connection
        connection = Mockito.mock(WebsocketConnection.class);

        // construct twitchChat
        twitchChat = TwitchChatBuilder.builder()
                .withWebsocketConnection(connection)
                .withChatAccount(TestUtils.getCredential())
                .withCommandTrigger("!")
                .build();
    }

    @Test
    public void testJoinRateLimit() {
        // fake connected state
        Mockito.when(connection.getConnectionState()).thenReturn(WebsocketConnectionState.CONNECTED);

        // join a bunch of channels
        for (int i = 1; i <= 100; i++) {
            twitchChat.joinChannel("twitch4j" + i);
        }

        // wait
        await().atMost(Duration.ofSeconds(1)).until(() -> twitchChat.ircCommandQueue.size() <= 80);

        // verify
        Mockito.verify(connection, times(20)).sendText(startsWith("JOIN #twitch4j"));
    }

    @Test
    public void testChannelMessageRateLimit() {
        // fake connected state
        Mockito.when(connection.getConnectionState()).thenReturn(WebsocketConnectionState.CONNECTED);

        // join a bunch of messages
        for (int i = 1; i <= 100; i++) {
            twitchChat.sendMessage("twitch4j", "Hello @twitch4j");
        }

        // wait for all commands to be processed
        await().atMost(Duration.ofSeconds(1)).until(() -> twitchChat.ircCommandQueue.size() <= 80);

        // verify
        Assertions.assertEquals(0, twitchChat.ircCommandQueue.size(), "there shouldn't be any queued messages left");
        Mockito.verify(connection, times(20)).sendText(eq("PRIVMSG #twitch4j :Hello @twitch4j"));
    }

    @Test
    public void testAuthRateLimit() {
        // fake disconnected state
        Mockito.when(connection.getConnectionState()).thenReturn(WebsocketConnectionState.DISCONNECTED);

        // connect a few times
        for (int i = 1; i <= 20; i++) {
            twitchChat.connect();
        }

        // verify
        Assertions.assertEquals(0, twitchChat.ircAuthBucket.getAvailableTokens(), "we should have used up all auth tokens");
    }

}
