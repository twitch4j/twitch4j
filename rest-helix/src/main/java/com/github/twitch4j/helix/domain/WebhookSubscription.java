package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Webhook Subscription
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Deprecated
public class WebhookSubscription {

    /**
     * The callback provided for this subscription.
     */
    private String callback;

    /**
     * Date and time when this subscription expires. Encoded as RFC3339. The timezone is always UTC ("Z").
     */
    private Instant expiresAt;

    /**
     * The topic used in the initial subscription.
     */
    private String topic;

    /**
     * @return date and time when this subscription expires.
     * @deprecated in favor of #getExpiresAt()
     */
    @Deprecated
    public String getExpires_at() {
        return expiresAt.toString();
    }

}
