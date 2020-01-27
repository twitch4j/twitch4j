package com.github.twitch4j.pubsub;

import com.github.twitch4j.pubsub.domain.PubSubRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PubSub subscription.
 * Can be used to unsubscribe from a topic
 *
 * @see TwitchPubSub#unsubscribeFromTopic(PubSubSubscription)
 */
@RequiredArgsConstructor
public class PubSubSubscription {
    @Getter(AccessLevel.PACKAGE)
    private final PubSubRequest request;
}
