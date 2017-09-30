package me.philippheuer.twitch4j.message.pubsub.topics;

import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.PubSubTopic;

import java.util.Optional;

public class Commerce extends PubSubTopics {

    public Commerce(Optional<OAuthCredential> credential) {
        super(PubSubTopic.COMMERCE, credential);
    }

    @Override
    public String stringify() {
        return String.format("%s.%s", getTopic().getPrefix(), getCredential().getUserId());
    }
}
