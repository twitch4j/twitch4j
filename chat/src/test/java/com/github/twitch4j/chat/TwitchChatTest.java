package com.github.twitch4j.chat;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class TwitchChatTest {

    private static TwitchChat twitchChat;

    @BeforeAll
    public static void connectToChat() {
        // external event manager
        EventManager eventManager = new EventManager();

        // construct twitchChat
        twitchChat = TwitchChatBuilder.builder()
            .withEventManager(eventManager)
            .withChatAccount(TestUtils.getCredential())
            .build();

        // sleep for a few seconds so that we're connected
        TestUtils.sleepFor(5000);
    }

    @Test
    @DisplayName("Tests sending and receiving channel messages")
    public void sendTwitchChannelMessage() {
        // listen for events in channel
        List<ChannelMessageEvent> channelMessages = new ArrayList<>();
        twitchChat.joinChannel("twitch4j");
        twitchChat.getEventManager().onEvent(ChannelMessageEvent.class).subscribe(event -> {
            channelMessages.add(event);
            log.debug(event.toString());
        });

        // send message to channel
        twitchChat.sendMessage("twitch4j", "Hello @twitch4j");

        // sleep a second and look of the message was sended
        TestUtils.sleepFor(1000);

        // check if the message was send and received
        assertTrue(twitchChat.ircCommandQueue.size() == 0, "Can't find the message we send in the received messages!");
    }

}
