package com.github.twitch4j.helix.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Webhook Subscription
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookSubscription {
    
    /**
     *  The callback provided for this subscription.
     */
    private String callback;
    
    /**
     * 	Date and time when this subscription expires. Encoded as RFC3339. The timezone is always UTC (“Z”).
     */
    private String expires_at;
    
    /**
     *  The topic used in the initial subscription.
     */
    private String topic;
    
}
