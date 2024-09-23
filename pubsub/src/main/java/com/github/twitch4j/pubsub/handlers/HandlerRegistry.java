package com.github.twitch4j.pubsub.handlers;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Getter(onMethod_ = { @ApiStatus.Internal })
public enum HandlerRegistry {
    INSTANCE;

    private final Map<String, TopicHandler> handlers;

    HandlerRegistry() {
        Map<String, TopicHandler> map = new HashMap<>();
        Stream.of(
            // official topics
            new AutoModQueueHandler(),
            new BitsHandler(),
            new BitsBadgeHandler(),
            new ChannelPointsHandler(),
            new ModeratorHandler(),
            new SubscriptionHandler(),
            new SuspiciousHandler(),
            new UserModeratedHandler(),
            new WhispersHandler(),

            // unofficial topics
            new AdsHandler(),
            new AdsManagerHandler(),
            new AutoModLevelHandler(),
            new BoostHandler(),
            new BroadcastSettingsHandler(),
            new CharityHandler(),
            new ChatroomHandler(),
            new CheerbombHandler(),
            new FollowingHandler(),
            new FollowsHandler(),
            new GiftHandler(),
            new GoalsHandler(),
            new HighlightHandler(),
            new LeaderboardHandler(),
            new NotificationHandler(),
            new PinHandler(),
            new PlaybackHandler(),
            new PollsHandler(),
            new PredictionHandler(),
            new RaidHandler(),
            new SharedChatHandler(),
            new ShieldHandler(),
            new ShoutoutHandler(),
            new TrainHandler(),
            new UnbanRequestHandler(),
            new UserPointsHandler(),
            new UserPredictionHandler(),
            new UserUnbanRequestHandler()
        ).forEach(handler -> handler.topicNames().forEach(topic -> map.put(topic, handler)));
        this.handlers = Collections.unmodifiableMap(map);
    }
}
