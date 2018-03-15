@EventProcessor
@Value.Include({
        ActionMessageEvent.class,
        BanEvent.class,
        BitsMessageEvent.class,
        ChannelEvent.class,
        ChannelUserEvent.class,
        ClearChatEvent.class,
        GlobalUserStateEvent.class,
        HostEvent.class,
        IrcEvent.class,
        JoinChannelEvent.class,
        MessageEvent.class,
        NewChatterEvent.class,
        NoticeEvent.class,
        OrdinalMessageEvent.class,
        PartChannelEvent.class,
        PingReceivedEvent.class,
        PongReceivedEvent.class,
        PrivateMessageEvent.class,
        RaidEvent.class,
        RitualNoticeEvent.class,
        RoomStateChangedEvent.class,
        RoomStateEvent.class,
        SubscribeEvent.class,
        TimeoutEvent.class,
        UnhostEvent.class,
        UserNoticeEvent.class,
        UserStateEvent.class
})
package io.twitch4j.impl.events.tmi;

import io.twitch4j.annotation.EventProcessor;
import io.twitch4j.events.tmi.*;
import org.immutables.value.Value;