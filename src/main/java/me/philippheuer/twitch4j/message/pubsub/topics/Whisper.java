package me.philippheuer.twitch4j.message.pubsub.topics;

import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.PubSubTopic;

import java.util.Optional;

public class Whisper extends PubSubTopics {
    public Whisper(Optional<OAuthCredential> credential) {
        super(PubSubTopic.WHISPERS, credential);
    }

    @Override
    public String stringify() {
        return String.format("%s.%s", getTopic().getPrefix(), getCredential().getUserId());
    }
}
