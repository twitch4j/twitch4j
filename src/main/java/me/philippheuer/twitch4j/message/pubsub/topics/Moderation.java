package me.philippheuer.twitch4j.message.pubsub.topics;

import me.philippheuer.twitch4j.enums.PubSubTopic;
import lombok.Getter;
import lombok.NonNull;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.model.Channel;

import java.util.Optional;

@Getter
public class Moderation extends PubSubTopics {
    public Moderation(Optional<OAuthCredential> credential, @NonNull Channel channel) {
        super(PubSubTopic.MODERATION, credential, channel);
    }

    public String stringify() {
        return String.format("%s.%s.%s", getTopic().getPrefix(), getCredential().getUserId(), getChannel().getId());
    }
}
