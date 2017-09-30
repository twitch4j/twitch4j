package me.philippheuer.twitch4j.message.pubsub;

import me.philippheuer.twitch4j.message.pubsub.topics.PubSubTopics;

public class PubSubReachedLimitException extends RuntimeException {
    public PubSubReachedLimitException(PubSubTopics topic) {
        super(String.format("Couldn't add topic %s for %s, cause you are reached limits of listened topics.", topic.toString(), topic.getCredential().getUserName()));
    }
}
