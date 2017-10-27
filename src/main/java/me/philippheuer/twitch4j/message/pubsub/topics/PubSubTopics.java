package me.philippheuer.twitch4j.message.pubsub.topics;

import me.philippheuer.twitch4j.enums.PubSubTopic;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.model.Channel;
import org.springframework.util.Assert;

import java.util.Optional;

@Getter(AccessLevel.PACKAGE)
public abstract class PubSubTopics {

    private final PubSubTopic topic;
    @Getter(AccessLevel.PUBLIC)
    private final OAuthCredential credential;
    private final Channel channel;

    public PubSubTopics(PubSubTopic topic, Optional<OAuthCredential> credential) {
        this.topic = topic;
        if (isOAuthRequired())
            Assert.isTrue(credential.isPresent(), "Required Credentials for specified topic!");
        this.credential = credential.orElse(null);

        if (this.credential != null) {
            Assert.isTrue(this.topic.isRequiredScope(this.credential.getOAuthScopes()), String.format("Cannot listening topic %s. OAuth is not in required scope %s", toString(), topic.getScopes().get(0).getKey()));
        }
        this.channel = null;
    }

    public PubSubTopics(PubSubTopic topic, Optional<OAuthCredential> credential, @NonNull Channel channel) {
        this.topic = topic;
        this.channel = channel;
        if (isOAuthRequired())
            Assert.isTrue(credential.isPresent(), "Required Credentials for specified topic!");
        this.credential = credential.orElse(null);

        if (this.credential != null) {
            Assert.isTrue(this.topic.isRequiredScope(this.credential.getOAuthScopes()), String.format("Cannot listening topic %s. OAuth is not in required scope %s", toString(), topic.getScopes().get(0).getKey()));
        }
    }

    @Override
    public String toString() {
        return stringify();
    }

    public boolean isOAuthRequired() {
        return topic.isScopesRequired();
    }

    public abstract String stringify();
}
