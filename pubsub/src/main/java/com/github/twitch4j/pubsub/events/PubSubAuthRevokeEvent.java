package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.ITwitchPubSub;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * Fired when a user access token had outstanding PubSub subscriptions, but was revoked.
 * <p>
 * An authorization can be revoked at any time for a number of reasons,
 * such a user disconnecting from an app through <a href="https://www.twitch.tv/settings/connections">Connections Settings</a>.
 * <p>
 * Topics associated with the authorization will automatically be unlistened to.
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class PubSubAuthRevokeEvent extends TwitchEvent {
    ITwitchPubSub socket;
    Map<@NotNull String, @Nullable PubSubRequest> revokedListensByTopic;

    public Collection<String> getTopicNames() {
        return revokedListensByTopic.keySet();
    }
}
