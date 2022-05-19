package com.github.twitch4j;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("unittest")
public class TwitchClientTest {

    @Test
    @DisplayName("Tests the TwitchClientBuilder")
    public void buildTwitch4J() {
        // external event manager
        EventManager eventManager = new EventManager();
        eventManager.autoDiscovery();
        eventManager.setDefaultEventHandler(SimpleEventHandler.class);

        // construct twitchClient
        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEventManager(eventManager)
            .withEnableHelix(true)
            .withEnableKraken(true)
            .withEnableChat(false)
            .build();
    }

    @Test
    @DisplayName("Ensure builder calls don't reset command triggers")
    public void buildCommandPrefix() {
        TwitchClientBuilder b1 = TwitchClientBuilder.builder().withEnableChat(true);
        assertTrue(b1.getCommandPrefixes().isEmpty());

        TwitchClientBuilder b2 = b1.withCommandTrigger("!");
        assertEquals(1, b2.getCommandPrefixes().size());

        TwitchClientBuilder b3 = b2.withEnablePubSub(true);
        assertEquals(1, b3.getCommandPrefixes().size());
    }

    @Test
    @DisplayName("Test if the Twitch4J ThreadPool is closed on shutdown")
    public void testScheduledThreadPoolExecutorShutdown() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("twitch4j", 10);

        // construct twitchClient
        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEnableHelix(true)
            .withEnableKraken(true)
            .withEnableChat(false)
            .withScheduledThreadPoolExecutor(scheduledThreadPoolExecutor)
            .build();
        twitchClient.close();

        assertTrue(scheduledThreadPoolExecutor.isShutdown(), "ThreadPool should have been closed!");
    }

    @Test
    @DisplayName("Test if externally provided scheduledThreadPoolExecutor are still alive after closing Twitch4J")
    public void testScheduledThreadPoolExecutorExternalKeepAlive() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("twitch4j", 10);

        // construct twitchClient
        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEnableHelix(true)
            .withEnableKraken(true)
            .withEnableChat(false)
            .withScheduledThreadPoolExecutor(new ScheduledThreadPoolExecutor(1))
            .build();
        twitchClient.close();

        assertTrue(!scheduledThreadPoolExecutor.isShutdown(), "ThreadPool should have been closed!");
    }

    /**
     * Debugging
     */
    @Test
    @DisplayName("A customizable test wireframe")
    @Disabled
    public void localTest() throws Exception {
        // construct twitchClient
        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEventManager(null)
            .withEnableHelix(true)
            .withEnableKraken(false)
            .withEnableTMI(false)
            .withEnableChat(true)
            .withChatAccount(TestUtils.getCredential())
            .withDefaultAuthToken(TestUtils.getCredential())
            .withEnablePubSub(true)
            .withEnableGraphQL(false)
            .withHelperThreadDelay(10000L)
            .build();

        // your code here
    }

}
