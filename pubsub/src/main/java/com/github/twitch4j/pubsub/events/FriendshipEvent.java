package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.FriendshipData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @deprecated Friends are being removed by <a href="https://help.twitch.tv/s/article/how-to-use-the-friends-feature">Twitch</a> on 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class FriendshipEvent extends TwitchEvent {
    private final FriendshipData data;
}
