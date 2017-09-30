package me.philippheuer.twitch4j.message.pubsub.topics;

import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.PubSubTopic;

import java.util.Optional;

public class Subscribe extends PubSubTopics {

    public Subscribe(Optional<OAuthCredential> credential) {
        super(PubSubTopic.SUBSCRIBE, credential);
    }

    @Override
    public String stringify() {
        return String.format("%s.%s", getTopic().getPrefix(), getCredential().getUserId());
    }
}
