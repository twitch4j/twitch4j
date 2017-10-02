package me.philippheuer.twitch4j.message.pubsub.topics;

import me.philippheuer.twitch4j.enums.PubSubTopic;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;

import java.util.Optional;

public class Bits extends PubSubTopics {

    public Bits(Optional<OAuthCredential> credential) {
        super(PubSubTopic.BITS, credential);
    }

    @Override
    public String stringify() {
        return String.format("%s.%s", getTopic().getPrefix(), getCredential().getUserId());
    }
}
