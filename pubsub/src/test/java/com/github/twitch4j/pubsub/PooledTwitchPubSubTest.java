package com.github.twitch4j.pubsub;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.TestUtils;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.enums.PubSubType;
import com.github.twitch4j.pubsub.events.RaidGoEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Tag("integration")
public class PooledTwitchPubSubTest {

    private static TwitchPubSubConnectionPool pool;

    @BeforeAll
    public static void createPool() {
        pool = TwitchPubSubConnectionPool.builder()
            .maxSubscriptionsPerConnection(3) // excessively low just for testing purposes
            .build();

        pool.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(RaidGoEvent.class, System.out::println);
    }

    @Test
    @DisplayName("Test the basic lifecycle of a PubSub Connection Pool")
    public void basicLifecycle() {
        assertEquals(0, pool.numConnections());
        assertEquals(0, pool.numSubscriptions());

        PubSubSubscription f1 = pool.listenForRaidEvents(null, "207813352");
        assertEquals(1, pool.numConnections());
        assertEquals(1, pool.numSubscriptions());

        PubSubSubscription f2 = pool.listenForRaidEvents(null, "22025290");
        assertEquals(1, pool.numConnections());
        assertEquals(2, pool.numSubscriptions());

        PubSubSubscription f3 = pool.listenForRaidEvents(null, "35759863");
        assertEquals(1, pool.numConnections());
        assertEquals(3, pool.numSubscriptions());

        PubSubSubscription f4 = pool.listenForRaidEvents(null, "468488256");
        assertEquals(2, pool.numConnections());
        assertEquals(4, pool.numSubscriptions());

        PubSubSubscription f5 = pool.listenForRaidEvents(null, "269275547");
        assertEquals(2, pool.numConnections());
        assertEquals(5, pool.numSubscriptions());

        PubSubSubscription f6 = pool.listenForRaidEvents(null, "450107466");
        assertEquals(2, pool.numConnections());
        assertEquals(6, pool.numSubscriptions());

        PubSubSubscription f7 = pool.listenForRaidEvents(null, "40197643");
        assertEquals(3, pool.numConnections());
        assertEquals(7, pool.numSubscriptions());

        TestUtils.sleepFor(5000);

        assertNotNull(pool.unsubscribe(f7));
        assertEquals(2, pool.numConnections());
        assertEquals(6, pool.numSubscriptions());

        assertNotNull(pool.unsubscribe(f5));
        assertEquals(2, pool.numConnections());
        assertEquals(5, pool.numSubscriptions());

        assertNotNull(pool.unsubscribe(f3));
        assertEquals(2, pool.numConnections());
        assertEquals(4, pool.numSubscriptions());

        assertNotNull(pool.unsubscribe(f4));
        assertEquals(2, pool.numConnections());
        assertEquals(3, pool.numSubscriptions());

        assertNotNull(pool.unsubscribe(f6));
        assertEquals(1, pool.numConnections());
        assertEquals(2, pool.numSubscriptions());

        assertNotNull(pool.unsubscribe(f1));
        assertEquals(1, pool.numConnections());
        assertEquals(1, pool.numSubscriptions());

        assertNotNull(pool.unsubscribe(f2));
        assertEquals(0, pool.numConnections());
        assertEquals(0, pool.numSubscriptions());

        PubSubSubscription f8 = pool.subscribe(createListenToRaidsReq("134214675", "91693482"));
        assertEquals(1, pool.numConnections());
        assertEquals(2, pool.numSubscriptions());

        TestUtils.sleepFor(5000);

        assertNotNull(pool.unsubscribe(f8));
        assertEquals(0, pool.numConnections());
        assertEquals(0, pool.numSubscriptions());

        TestUtils.sleepFor(5000);
    }

    private static PubSubRequest createListenToRaidsReq(String... channelIds) {
        PubSubRequest req = new PubSubRequest();
        req.setType(PubSubType.LISTEN);
        req.setNonce(CryptoUtils.generateNonce(30));
        req.getData().put("topics", Arrays.stream(channelIds).map(id -> "raid." + id).collect(Collectors.toList()));
        return req;
    }

}
