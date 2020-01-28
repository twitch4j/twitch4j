package com.github.twitch4j.pubsub;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

@Slf4j
@Tag("integration")
public class TwitchPubSubTest {

    private static TwitchPubSub twitchPubSub;

    @BeforeAll
    public static void connectToChat() {
        // external event manager
        EventManager eventManager = new EventManager();

        // construct twitchChat
        twitchPubSub = TwitchPubSubBuilder.builder()
            .withEventManager(eventManager)
            .build();

        // sleep for a few seconds so that we're connected
        TestUtils.sleepFor(5000);
    }

    @Test
    @DisplayName("Local test to keep it running for debugging")
    @Disabled
    public void localTestRun() {
        // listen for events in channel
        twitchPubSub.listenForCheerEvents(TestUtils.getCredential(), "149223493");
        twitchPubSub.listenForCommerceEvents(TestUtils.getCredential(), "149223493");
        twitchPubSub.listenForSubscriptionEvents(TestUtils.getCredential(), "149223493");
        twitchPubSub.listenForWhisperEvents(TestUtils.getCredential(), "149223493");

        // sleep a second and look of the message was sended
        TestUtils.sleepFor(60000);
    }

}
