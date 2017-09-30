package me.philippheuer.twitch4j.message.pubsub.topics;

import lombok.Getter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.enums.PubSubTopic;
import me.philippheuer.twitch4j.model.Channel;

import java.util.Optional;

@Getter
public class Moderation extends PubSubTopics {
    private final Channel channel;

    public Moderation(Optional<OAuthCredential> credential, Channel channel) {
        super(PubSubTopic.MODERATION, credential);
        this.channel = channel;
    }

    @Override
    public String stringify() {
        return String.format("%s.%s.%s", getTopic().getPrefix(), getCredential().getUserId(), channel.getId());
    }
}
