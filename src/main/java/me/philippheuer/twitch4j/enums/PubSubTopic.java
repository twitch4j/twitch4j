package me.philippheuer.twitch4j.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
public enum PubSubTopic {
    WHISPERS("whispers", TwitchScopes.CHAT_LOGIN),
    VIDEO_PLAYBACK("video-playback", false),
    BITS("channel-bits-events-v1", true),
    SUBSCRIBE("channel-subscribe-events-v1", TwitchScopes.CHANNEL_SUBSCRIPTIONS),
    COMMERCE("channel-commerce-events-v1", true),
    MODERATION("chat_moderator_actions", TwitchScopes.CHAT_LOGIN);

    private final String prefix;
    private final List<TwitchScopes> scopes = new ArrayList<TwitchScopes>();
    private final boolean scopesRequired;

    PubSubTopic(String topic, boolean required) {
        this.prefix = topic;
        this.scopesRequired = required;
        if (required)
            this.scopes.addAll(Arrays.asList(TwitchScopes.values()));
    }

    PubSubTopic(String topic, TwitchScopes... scopes) {
        this.prefix = topic;
        this.scopesRequired = (scopes.length > 0);
        if (this.scopesRequired)
            this.scopes.addAll(Arrays.asList(scopes));

    }

    public boolean isRequiredScope(Set<String> scopes) {
        return scopes.size() != 1 || scopes.stream().map(scope -> scope.equals(this.scopes.get(0).getKey())).toArray().length > 0;
    }
}
