package com.github.twitch4j.pubsub;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.util.CryptoUtils;
import com.github.twitch4j.common.util.TestUtils;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.enums.PubSubType;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;

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

        pool.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(FollowingEvent.class, System.out::println);
    }

    @Test
    @DisplayName("Test the basic lifecycle of a PubSub Connection Pool")
    public void basicLifecycle() {
        assertEquals(0, pool.numConnections());
        assertEquals(0, pool.numSubscriptions());

        PubSubSubscription f1 = pool.subscribe(createListenToFollowsReq("207813352"));
        assertEquals(1, pool.numConnections());
        assertEquals(1, pool.numSubscriptions());

        PubSubSubscription f2 = pool.subscribe(createListenToFollowsReq("22025290"));
        assertEquals(1, pool.numConnections());
        assertEquals(2, pool.numSubscriptions());

        PubSubSubscription f3 = pool.subscribe(createListenToFollowsReq("35759863"));
        assertEquals(1, pool.numConnections());
        assertEquals(3, pool.numSubscriptions());

        PubSubSubscription f4 = pool.subscribe(createListenToFollowsReq("468488256"));
        assertEquals(2, pool.numConnections());
        assertEquals(4, pool.numSubscriptions());

        PubSubSubscription f5 = pool.subscribe(createListenToFollowsReq("269275547"));
        assertEquals(2, pool.numConnections());
        assertEquals(5, pool.numSubscriptions());

        PubSubSubscription f6 = pool.subscribe(createListenToFollowsReq("450107466"));
        assertEquals(2, pool.numConnections());
        assertEquals(6, pool.numSubscriptions());

        PubSubSubscription f7 = pool.subscribe(createListenToFollowsReq("40197643"));
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

        TestUtils.sleepFor(5000);
    }

    private static PubSubRequest createListenToFollowsReq(String channelId) {
        PubSubRequest req = new PubSubRequest();
        req.setType(PubSubType.LISTEN);
        req.setNonce(CryptoUtils.generateNonce(32));
        req.getData().put("topics", Collections.singletonList("following." + channelId));
        return req;
    }

}
