package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.domain.WebhookSubscriptionList;
import com.github.twitch4j.helix.webhooks.domain.WebhookRequest;
import com.github.twitch4j.helix.webhooks.topics.StreamsTopic;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.twitch4j.helix.TestUtils.sleepFor;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class WebhooksServiceTest extends AbstractEndpointTest {

    // UserId
    private static String twitchUserId = "149223493";

    @Test
    @DisplayName("Request a Stream Changed webhook subscription and verify using Get Webhook Subscriptions")
    public void getStreams() {
        WebhookRequest request = new WebhookRequest(
            System.getenv("CALLBACK_URL"),
            WebhookRequest.MODE_SUBSCRIBE,
            new StreamsTopic(twitchUserId),
            120,
            null
        );

        Response response = testUtils.getTwitchHelixClient().requestWebhookSubscription(
            request,
            System.getenv("APP_ACCESS_TOKEN")
        ).execute();
        assertTrue(response.status() >= 200 && response.status() < 300, "Response was: " + response.toString());

        sleepFor(30 * 1000);

        WebhookSubscriptionList subList = testUtils.getTwitchHelixClient().getWebhookSubscriptions(
            System.getenv("APP_ACCESS_TOKEN"),
            null,
            null
        ).execute();
        assertTrue(subList.getSubscriptions().size() > 0, "Sub list size was " + subList.getSubscriptions().size());

    }

}
