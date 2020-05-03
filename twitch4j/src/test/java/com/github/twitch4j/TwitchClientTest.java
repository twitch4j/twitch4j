package com.github.twitch4j;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("unittest")
public class TwitchClientTest {

    @Test
    @DisplayName("Tests the TwitchClientBuilder")
    public void buildTwitch4J() {
        // external event manager (for shared module usage - streamlabs4j)
        EventManager eventManager = new EventManager();

        // construct twitchClient
        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEventManager(eventManager)
            .withEnableHelix(true)
            .withEnableKraken(true)
            .withEnableChat(false)
            .build();
    }

    @Test
    @DisplayName("Test if the Twitch4J ThreadPool is closed on shutdown")
    public void testScheduledThreadPoolExecutorShutdown() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor();

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
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = ThreadUtils.getDefaultScheduledThreadPoolExecutor();

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
            .withEnableChat(false)
            .withChatAccount(TestUtils.getCredential())
            .withEnablePubSub(false)
            .withEnableGraphQL(false)
            .withScheduledThreadPoolExecutor(new ScheduledThreadPoolExecutor(1))
            .withHelperThreadRate(10000L)
            .build();

        // code here
    }

}
