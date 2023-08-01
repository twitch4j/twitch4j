package com.github.twitch4j.pubsub.events;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.BanSharingSettings;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Fired when a channel changes their ban sharing settings.
 * <p>
 * This includes changing the low trust treatment that is applied to users with shared bans,
 * or changing which types of users can request the broadcaster to share their bans.
 * <p>
 * Requires {@link com.github.twitch4j.pubsub.ITwitchPubSub#listenForLowTrustUsersEvents(OAuth2Credential, String, String)}.
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class BanSharingSettingsUpdateEvent extends TwitchEvent {
    String moderatorId;
    String channelId;
    BanSharingSettings data;
}
