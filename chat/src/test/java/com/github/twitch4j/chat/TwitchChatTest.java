package com.github.twitch4j.chat;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.chat.util.TestUtils;
import com.github.twitch4j.client.websocket.WebsocketConnection;
import com.github.twitch4j.client.websocket.domain.WebsocketConnectionState;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.test.TestEventManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@Slf4j
public class TwitchChatTest {

    private WebsocketConnection connection;

    private TestEventManager eventManager;

    private TwitchChat twitchChat;

    @BeforeEach
    void init() {
        // mock connection
        connection = Mockito.mock(WebsocketConnection.class);

        // mock eventManager
        eventManager = new TestEventManager();

        // construct twitchChat
        twitchChat = TwitchChatBuilder.builder()
                .withWebsocketConnection(connection)
                .withEventManager(eventManager)
                .withChatAccount(TestUtils.getCredential())
                .withCommandTrigger("!")
                .build();
    }

    @Test
    public void testJoinChannel() {
        // fake connected state
        Mockito.when(connection.getConnectionState()).thenReturn(WebsocketConnectionState.CONNECTED);

        // join channel
        twitchChat.joinChannel("twitch4j");

        // wait for all commands to be processed
        await().atMost(Duration.ofSeconds(1)).until(() -> twitchChat.ircCommandQueue.size() == 0);

        // verify
        Assertions.assertEquals(WebsocketConnectionState.CONNECTED, connection.getConnectionState(), "should be CONNECTED");
        Assertions.assertEquals(0, twitchChat.ircCommandQueue.size(), "there shouldn't be any queued messages left");
        Mockito.verify(connection, times(1)).sendText(eq("JOIN #twitch4j"));
    }

    @Test
    public void testSendChannelMessage() {
        // fake connected state
        Mockito.when(connection.getConnectionState()).thenReturn(WebsocketConnectionState.CONNECTED);

        // send messages
        twitchChat.sendMessage("twitch4j", "Hello @twitch4j");

        // wait for all commands to be processed
        await().atMost(Duration.ofSeconds(1)).until(() -> twitchChat.ircCommandQueue.size() == 0);

        // verify
        Assertions.assertEquals(WebsocketConnectionState.CONNECTED, connection.getConnectionState(), "should be CONNECTED");
        Assertions.assertEquals(0, twitchChat.ircCommandQueue.size(), "there shouldn't be any queued messages left");
        Mockito.verify(connection, times(1)).sendText(eq("PRIVMSG #twitch4j :Hello @twitch4j"));
    }

    @Test
    public void testReceiveChannelMessage() {
        // simulate a message
        twitchChat.onTextMessage("@badge-info=;badges=moments/1;client-nonce=2a752cf1b27d354c11cbc1b845229091;color=#00FF7F;display-name=Twitch4J;emotes=;first-msg=0;flags=;id=7bb22cd5-4882-4d79-b12f-8c9473004542;mod=0;room-id=149223493;subscriber=0;tmi-sent-ts=1647099473133;turbo=0;user-id=149223493;user-type= :twitch4j!twitch4j@twitch4j.tmi.twitch.tv PRIVMSG #twitch4j :hello world");

        // expect a IRCMessageEvent and ChannelMessageEvent
        Assertions.assertEquals(2, eventManager.getPublishedEvents().size());
        Assertions.assertTrue(eventManager.getPublishedEvents().get(0) instanceof IRCMessageEvent);
        Assertions.assertTrue(eventManager.getPublishedEvents().get(1) instanceof ChannelMessageEvent);
        ChannelMessageEvent event = ((ChannelMessageEvent) eventManager.getPublishedEvents().get(1));
        Assertions.assertEquals("2a752cf1b27d354c11cbc1b845229091", event.getNonce());
        Assertions.assertEquals(false, event.isDesignatedFirstMessage());
        Assertions.assertTrue(event.getPermissions().contains(CommandPermission.EVERYONE));
        Assertions.assertEquals("twitch4j", event.getChannel().getName());
        Assertions.assertEquals("twitch4j", event.getUser().getName());
        Assertions.assertEquals("Twitch4J", event.getMessageEvent().getRawTagString("display-name"));
        Assertions.assertEquals("hello world", event.getMessage());
    }

}
