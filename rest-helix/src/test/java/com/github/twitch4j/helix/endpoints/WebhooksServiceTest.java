package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.eventsub.EventSubTransport;
import com.github.twitch4j.eventsub.EventSubTransportMethod;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes;
import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.EventSubSubscriptionList;
import com.netflix.hystrix.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class WebhooksServiceTest extends AbstractEndpointTest {

    // UserId
    private static String twitchUserId = "149223493";

    @Test
    @DisplayName("Request a Stream Online EventSub webhook subscription and verify using Get EventSub Subscriptions")
    @Disabled
    public void getStreams() {
        EventSubTransport transport = EventSubTransport.builder()
            .method(EventSubTransportMethod.WEBHOOK)
            .callback(System.getenv("CALLBACK_URL"))
            .secret("twitch4j-is-not-a-good-secret")
            .build();

        HystrixCommand<EventSubSubscriptionList> createCommand = TestUtils.getTwitchHelixClient().createEventSubSubscription(
            System.getenv("APP_ACCESS_TOKEN"),
            SubscriptionTypes.STREAM_ONLINE.prepareSubscription(b -> b.broadcasterUserId(twitchUserId).build(), transport)
        );
        assertDoesNotThrow(createCommand::execute, "Failed to create EventSub subscription");

        EventSubSubscriptionList subList = TestUtils.getTwitchHelixClient().getEventSubSubscriptions(
            System.getenv("APP_ACCESS_TOKEN"),
            null,
            null,
            null,
            null,
            null
        ).execute();
        assertTrue(subList.getTotal() > 0, "Sub list size was " + subList.getTotal());
    }

}
