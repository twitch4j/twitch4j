package com.github.twitch4j.pubsub.events;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.ITwitchPubSub;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import com.github.twitch4j.pubsub.enums.PubSubType;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;
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
    /**
     * The PubSub websocket that was subscribed to these revoked topics.
     */
    ITwitchPubSub socket;

    /**
     * Mappings of revoked topic names to the associated {@link PubSubRequest} (LISTEN).
     * <p>
     * Warning: Values can be null if you manually call
     * {@link ITwitchPubSub#listenOnTopic(PubSubRequest)} or
     * {@link ITwitchPubSub#listenOnTopic(PubSubType, OAuth2Credential, Collection)}
     * with requests that contain <b>more than</b> one topic in a <i>single</i> {@link PubSubType#LISTEN}.
     */
    @NotNull
    @ApiStatus.Experimental
    Map<@NotNull String, @Nullable PubSubRequest> revokedListensByTopic;

    /**
     * @return the topics that were revoked (including the user id's)
     */
    @NotNull
    public Collection<String> getTopicNames() {
        return revokedListensByTopic.keySet();
    }
}
