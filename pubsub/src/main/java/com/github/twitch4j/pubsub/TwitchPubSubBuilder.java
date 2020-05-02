package com.github.twitch4j.pubsub;

import com.github.philippheuer.events4j.core.EventManager;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Twitch PubSub Builder
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchPubSubBuilder {

    /**
     * Event Manager
     */
    @With
    private EventManager eventManager = new EventManager();

    /**
     * Initialize the builder
     * @return Twitch Chat Builder
     */
    public static TwitchPubSubBuilder builder() {
        return new TwitchPubSubBuilder();
    }

    /**
     * Twitch API Client (Helix)
     * @return TwitchHelix
     */
    public TwitchPubSub build() {
        log.debug("PubSub: Initializing Module ...");
        TwitchPubSub twitchPubSub = new TwitchPubSub(this.eventManager);

        return twitchPubSub;
    }

}
