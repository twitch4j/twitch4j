package com.github.twitch4j.chat;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelJoinFailureEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.chat.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@Tag("unittest")
public class ChatJoinRetryTest {

    private static final String FAKE_CHANNEL_NAME = "twitch4jtestchannelthatisnotreall"; // should exceed the max account length to make sure no such account can be created

    private static TwitchChat getChatInstance(int maxRetries, boolean removeChannelOnJoinFailure) {
        // spy on EventManager
        EventManager eventManager = new EventManager();
        eventManager.autoDiscovery();
        eventManager.setDefaultEventHandler(SimpleEventHandler.class); // synchronous execution is best suited for tests

        // spy on TwitchChat
        TwitchChat twitchChat = Mockito.spy(
            TwitchChatBuilder.builder()
                .withEventManager(Mockito.spy(eventManager))
                .withRemoveChannelOnJoinFailure(removeChannelOnJoinFailure)
                .withMaxJoinRetries(maxRetries)
                .withChatJoinTimeout(100L) // reduce the duration of test executions
                .build()
        );

        return twitchChat;
    }

    private static IRCMessageEvent testIRCMessageEvent(String raw) {
        return new IRCMessageEvent(raw, Collections.singletonMap("149223493", FAKE_CHANNEL_NAME), Collections.singletonMap(FAKE_CHANNEL_NAME, "149223493"), null);
    }

    @Test
    @DisplayName("successfully join a channel")
    public void testChannelJoinSuccess() {
        // init
        TwitchChat twitchChat = getChatInstance(1, false);

        // join channel and wait for the command to be processed
        twitchChat.joinChannel(FAKE_CHANNEL_NAME);
        verify(twitchChat, timeout(1_000)).issueJoin(FAKE_CHANNEL_NAME, 0);
        TestUtils.sleepFor(50);

        // check that we kept track of the join attempt in joinAttemptsByChannelName
        Assertions.assertNotNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be in joinAttemptsByChannelName while the join attempt is in an unknown state");

        // fake a successful join
        twitchChat.getEventManager().publish(testIRCMessageEvent("@emote-only=0;followers-only=-1;r9k=0;rituals=0;room-id=149223493;slow=0;subs-only=0 :tmi.twitch.tv ROOMSTATE #"+FAKE_CHANNEL_NAME));

        // should be gone from joinAttemptsByChannelName after successful join
        Assertions.assertNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be gone from joinAttemptsByChannelName after successful join");
        Assertions.assertTrue(twitchChat.currentChannels.contains(FAKE_CHANNEL_NAME), "channel should remain in currentChannels after successful join");

        // cleanup
        twitchChat.close();
    }

    @Test
    @DisplayName("failed to join a channel because of unknown reasons")
    public void testChannelJoinFailedByExpire() {
        // init
        TwitchChat twitchChat = getChatInstance(1, true);

        // join channel and wait for the command to be processed
        twitchChat.joinChannel(FAKE_CHANNEL_NAME);
        verify(twitchChat, timeout(1_000)).issueJoin(FAKE_CHANNEL_NAME, 0);
        TestUtils.sleepFor(50);

        // check that we kept track of the join attempt in joinAttemptsByChannelName
        Assertions.assertNotNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be in joinAttemptsByChannelName while the join attempt is in an unknown state");

        // should see a ChannelRemovedPostJoinFailureEvent and entry should have expired
        verify(twitchChat.getEventManager(), timeout(30_000)).publish(new ChannelJoinFailureEvent(FAKE_CHANNEL_NAME, ChannelJoinFailureEvent.Reason.RETRIES_EXHAUSTED));
        Assertions.assertNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be gone from joinAttemptsByChannelName after it expired");
        Assertions.assertFalse(twitchChat.currentChannels.contains(FAKE_CHANNEL_NAME), "channel should be gone from currentChannels after retries have been exhausted");

        // cleanup
        twitchChat.close();
    }

    @Test
    @DisplayName("fail to join a channel two times before successfully joining it")
    public void testChannelJoinFailedBeforeSuccess() {
        // init
        TwitchChat twitchChat = getChatInstance(3, true);

        // join channel and wait for the command to be processed
        twitchChat.joinChannel(FAKE_CHANNEL_NAME);
        verify(twitchChat, timeout(1_000)).issueJoin(FAKE_CHANNEL_NAME, 0);
        TestUtils.sleepFor(50);

        // check that we kept track of the join attempt in joinAttemptsByChannelName
        Assertions.assertNotNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be in joinAttemptsByChannelName while the join attempt is in an unknown state");

        // wait for 2 failed join attempts
        await().until(() -> Integer.valueOf(2).equals(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME)));

        // fake a successful join
        twitchChat.getEventManager().publish(testIRCMessageEvent("@emote-only=0;followers-only=-1;r9k=0;rituals=0;room-id=149223493;slow=0;subs-only=0 :tmi.twitch.tv ROOMSTATE #"+FAKE_CHANNEL_NAME));

        // should be gone from joinAttemptsByChannelName after successful join
        Assertions.assertNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be gone from joinAttemptsByChannelName after successful join");
        Assertions.assertTrue(twitchChat.currentChannels.contains(FAKE_CHANNEL_NAME), "channel should remain in currentChannels after successful join");

        // cleanup
        twitchChat.close();
    }

    @Test
    @DisplayName("failed to join a channel because we are banned")
    public void testChannelJoinFailedByBannedUser() {
        // init
        TwitchChat twitchChat = getChatInstance(1, true);

        // join channel and wait for the command to be processed
        twitchChat.joinChannel(FAKE_CHANNEL_NAME);
        verify(twitchChat, timeout(1_000)).issueJoin(FAKE_CHANNEL_NAME, 0);
        TestUtils.sleepFor(50);

        // check that we kept track of the join attempt in joinAttemptsByChannelName
        Assertions.assertNotNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be in joinAttemptsByChannelName while the join attempt is in an unknown state");

        // fake a banned notice
        twitchChat.getEventManager().publish(testIRCMessageEvent("@msg-id=msg_banned :tmi.twitch.tv NOTICE #"+FAKE_CHANNEL_NAME+" :You are permanently banned from talking in "+FAKE_CHANNEL_NAME+"."));

        // should be gone from joinAttemptsByChannelName because of the ban notice
        Assertions.assertNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be gone from joinAttemptsByChannelName after the ban notice");

        // should have received an event about the removal of the channel
        verify(twitchChat.getEventManager(), timeout(30_000)).publish(new ChannelJoinFailureEvent(FAKE_CHANNEL_NAME, ChannelJoinFailureEvent.Reason.USER_BANNED));
        Assertions.assertFalse(twitchChat.currentChannels.contains(FAKE_CHANNEL_NAME), "channel should be gone from currentChannels after the ban notice");

        // cleanup
        twitchChat.close();
    }

    @Test
    @DisplayName("failed to join a channel because the channel is suspended")
    public void testChannelJoinFailedChannelSuspended() {
        // init
        TwitchChat twitchChat = getChatInstance(1, true);

        // join channel and wait for the command to be processed
        twitchChat.joinChannel(FAKE_CHANNEL_NAME);
        verify(twitchChat, timeout(1_000)).issueJoin(FAKE_CHANNEL_NAME, 0);
        TestUtils.sleepFor(50);

        // check that we kept track of the join attempt in joinAttemptsByChannelName
        Assertions.assertNotNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be in joinAttemptsByChannelName while the join attempt is in an unknown state");

        // fake a banned notice
        twitchChat.getEventManager().publish(testIRCMessageEvent("@msg-id=msg_channel_suspended :tmi.twitch.tv NOTICE #"+FAKE_CHANNEL_NAME+" :This channel has been suspended."));

        // should be gone from joinAttemptsByChannelName because of the ban notice
        Assertions.assertNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be gone from joinAttemptsByChannelName after the channel suspension notice");

        // should have received an event about the removal of the channel
        verify(twitchChat.getEventManager(), timeout(30_000)).publish(new ChannelJoinFailureEvent(FAKE_CHANNEL_NAME, ChannelJoinFailureEvent.Reason.CHANNEL_SUSPENDED));
        Assertions.assertFalse(twitchChat.currentChannels.contains(FAKE_CHANNEL_NAME), "channel should be gone from currentChannels after the channel suspension notice");

        // cleanup
        twitchChat.close();
    }

    @Test
    @DisplayName("failed to join a channel because it was closed due to TOS violations")
    public void testChannelJoinFailedTOS() {
        // init
        TwitchChat twitchChat = getChatInstance(1, true);

        // join channel and wait for the command to be processed
        twitchChat.joinChannel(FAKE_CHANNEL_NAME);
        verify(twitchChat, timeout(1_000)).issueJoin(FAKE_CHANNEL_NAME, 0);
        TestUtils.sleepFor(50);

        // check that we kept track of the join attempt in joinAttemptsByChannelName
        Assertions.assertNotNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be in joinAttemptsByChannelName while the join attempt is in an unknown state");

        // fake a banned notice
        twitchChat.getEventManager().publish(testIRCMessageEvent("@msg-id=tos_ban :tmi.twitch.tv NOTICE #"+FAKE_CHANNEL_NAME+" :The community has closed channel "+FAKE_CHANNEL_NAME+" due to Terms of Service violations."));

        // should be gone from joinAttemptsByChannelName because of the ban notice
        Assertions.assertNull(twitchChat.joinAttemptsByChannelName.getIfPresent(FAKE_CHANNEL_NAME), "channel should be gone from joinAttemptsByChannelName after the channel suspension notice");

        // should have received an event about the removal of the channel
        verify(twitchChat.getEventManager(), timeout(30_000)).publish(new ChannelJoinFailureEvent(FAKE_CHANNEL_NAME, ChannelJoinFailureEvent.Reason.CHANNEL_SUSPENDED));
        Assertions.assertFalse(twitchChat.currentChannels.contains(FAKE_CHANNEL_NAME), "channel should be gone from currentChannels after the channel suspension notice");

        // cleanup
        twitchChat.close();
    }

}
