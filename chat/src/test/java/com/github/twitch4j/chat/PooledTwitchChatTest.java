package com.github.twitch4j.chat;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Tag("integration")
public class PooledTwitchChatTest {

    private static TwitchChatConnectionPool pool;

    @BeforeAll
    public static void createPool() {
        pool = TwitchChatConnectionPool.builder()
            .maxSubscriptionsPerConnection(2) // excessively low just for testing purposes
            .build();

        pool.getEventManager().getEventHandler(SimpleEventHandler.class)
            .onEvent(ChannelMessageEvent.class, e -> System.out.println(e.getChannel().getName() + " > " + e.getUser().getName() + ": " + e.getMessage()));
    }

    @Test
    @DisplayName("Test the basic lifecycle of a Chat Connection Pool")
    public void testLifecycle() {
        assertEquals(0, pool.numConnections());
        assertEquals(0, pool.numSubscriptions());

        pool.subscribe("hasanabi");
        pool.subscribe("lucidfoxx");
        pool.subscribe("themajorityreport");

        assertEquals(2, pool.numConnections());
        assertEquals(3, pool.numSubscriptions());
    }

    @Test
    @DisplayName("Local test to keep it running for debugging")
    @Disabled
    public void localTestRun() {
        // JOIN channel(s)
        pool.subscribe("hasanabi");
        pool.subscribe("lucidfoxx");
        pool.subscribe("themajorityreport");

        assertEquals(2, pool.numConnections());
        assertEquals(3, pool.numSubscriptions());

        // Sleep and monitor messages received
        TestUtils.sleepFor(60 * 1000);
    }
}
