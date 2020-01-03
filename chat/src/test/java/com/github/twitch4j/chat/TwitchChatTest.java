package com.github.twitch4j.chat;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.events.CommandEvent;
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
        // event manager
        EventManager eventManager = new EventManager();

        // credential manager
        CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
        credentialManager.registerIdentityProvider(new TwitchIdentityProvider("jzkbprff40iqj646a697cyrvl0zt2m6", "**SECRET**", ""));

        // construct twitchChat
        twitchChat = TwitchChatBuilder.builder()
            .withEventManager(eventManager)
            .withCredentialManager(credentialManager)
            .withChatAccount(TestUtils.getCredential())
            .withCommandTrigger("!")
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
        twitchChat.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelMessageEvent.class, event -> {
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

    @Test
    @DisplayName("Local test to keep it running for debugging")
    @Disabled
    public void localTestRun() {
        // listen for events in channel
        List<ChannelMessageEvent> channelMessages = new ArrayList<>();
        twitchChat.joinChannel("twitch4j");
        twitchChat.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelMessageEvent.class, event -> {
            log.debug(event.toString());
        });

        twitchChat.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(CommandEvent.class, event -> {
            log.debug(event.toString());
        });

        // sleep a second and look of the message was sended
        TestUtils.sleepFor(120000);
    }

}
