@ModelProcessor
@Value.Include({
        AuthorizationData.class,
        BlockedUser.class,
        Channel.class,
        ChatRoom.class,
        Clip.class,
        EmoteSet.class,
        Follow.class,
        Game.class,
        IngestServer.class,
        Kraken.class,
        Stream.class,
        Subscription.class,
        Team.class,
        Thumbnails.class,
        User.class,
        Video.class
})
package io.twitch4j.impl.api.kraken.model;

import io.twitch4j.annotation.ModelProcessor;
import io.twitch4j.api.kraken.model.*;
import org.immutables.value.Value;

