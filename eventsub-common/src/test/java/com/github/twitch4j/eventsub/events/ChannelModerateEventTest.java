package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.domain.moderation.Action;
import com.github.twitch4j.eventsub.domain.moderation.TermAction;
import com.github.twitch4j.eventsub.domain.moderation.TermType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unittest")
class ChannelModerateEventTest {

    @Test
    void deserializeMod() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"mod\",\"followers\":null,\"slow\":null,\"vip\":null,\"unvip\":null,\"mod\":{\"user_id\":\"141981764\",\"user_login\":\"twitchdev\",\"user_name\":\"TwitchDev\"},\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":null,\"unban_request\":null}",
            ChannelModerateEvent.class
        );
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("424596340", event.getModeratorUserId());
        assertEquals(Action.MOD, event.getAction());
        assertNotNull(event.getMod());
        assertEquals("141981764", event.getMod().getUserId());
    }

    @Test
    void deserializeTimeout() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"timeout\",\"followers\":null,\"slow\":null,\"vip\":null,\"unvip\":null,\"mod\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":{\"user_id\":\"141981764\",\"user_login\":\"twitchdev\",\"user_name\":\"TwitchDev\",\"reason\":\"Does not like pineapple on pizza.\",\"expires_at\":\"2022-03-15T02:00:28Z\"},\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":null,\"unban_request\":null}",
            ChannelModerateEvent.class
        );
        assertEquals("glowillig", event.getBroadcasterUserLogin());
        assertEquals("quotrok", event.getModeratorUserLogin());
        assertEquals(Action.TIMEOUT, event.getAction());
        assertNotNull(event.getTimeout());
        assertEquals("twitchdev", event.getTimeout().getUserLogin());
        assertEquals("Does not like pineapple on pizza.", event.getTimeout().getReason());
        assertEquals(Instant.parse("2022-03-15T02:00:28Z"), event.getTimeout().getExpiresAt());
    }

    @Test
    void deserializeEmoteOnly() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"emoteonly\",\"followers\":null,\"slow\":null,\"vip\":null,\"unvip\":null,\"mod\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":null,\"unban_request\":null}",
            ChannelModerateEvent.class
        );
        assertEquals("glowillig", event.getBroadcasterUserName());
        assertEquals("quotrok", event.getModeratorUserName());
        assertEquals(Action.EMOTEONLY, event.getAction());
    }

    @Test
    void deserializeFollowers() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"followers\",\"followers\":{\"follow_duration_minutes\":0},\"slow\":null,\"vip\":null,\"unvip\":null,\"mod\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":null,\"unban_request\":null}",
            ChannelModerateEvent.class
        );
        assertEquals(Action.FOLLOWERS, event.getAction());
        assertNotNull(event.getFollowers());
        assertEquals(0, event.getFollowers().getFollowDurationMinutes());
    }

    @Test
    void deserializeSlow() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"slow\",\"followers\":null,\"slow\":{\"wait_time_seconds\":3},\"vip\":null,\"unvip\":null,\"mod\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":null,\"unban_request\":null}",
            ChannelModerateEvent.class
        );
        assertEquals(Action.SLOW, event.getAction());
        assertNotNull(event.getSlow());
        assertEquals(3, event.getSlow().getWaitTimeSeconds());
    }

    @Test
    void deserializeDelete() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"delete\",\"followers\":null,\"slow\":null,\"vip\":null,\"unvip\":null,\"mod\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":{\"user_id\":\"268669435\",\"user_login\":\"djclancy\",\"user_name\":\"DJClancy\",\"message_id\":\"385bf4c2-d5f7-4542-b371-ec912fef32df\",\"message_body\":\"How do you do, fellow kids?\"},\"automod_terms\":null,\"unban_request\":null}",
            ChannelModerateEvent.class
        );
        assertEquals(Action.DELETE, event.getAction());
        assertNotNull(event.getDelete());
        assertEquals("DJClancy", event.getDelete().getUserName());
        assertEquals("385bf4c2-d5f7-4542-b371-ec912fef32df", event.getDelete().getMessageId());
        assertEquals("How do you do, fellow kids?", event.getDelete().getMessageBody());
    }

    @Test
    void deserializeTermAdd() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"add_permitted_term\",\"followers\":null,\"slow\":null,\"vip\":null,\"unvip\":null,\"mod\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":{\"action\":\"add\",\"list\":\"permitted\",\"terms\":[\"TriHard\"],\"from_automod\":true},\"unban_request\":null}",
            ChannelModerateEvent.class
        );
        assertEquals(Action.ADD_PERMITTED_TERM, event.getAction());
        assertNotNull(event.getAutomodTerms());
        assertEquals(TermAction.ADD, event.getAutomodTerms().getAction());
        assertEquals(TermType.PERMITTED, event.getAutomodTerms().getList());
        assertEquals(Collections.singletonList("TriHard"), event.getAutomodTerms().getTerms());
        assertTrue(event.getAutomodTerms().isFromAutomod());
    }

    @Test
    void deserializeTermRemove() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"remove_permitted_term\",\"followers\":null,\"slow\":null,\"vip\":null,\"unvip\":null,\"mod\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":{\"action\":\"remove\",\"list\":\"permitted\",\"terms\":[\"TriHard\"],\"from_automod\":false},\"unban_request\":null}",
            ChannelModerateEvent.class
        );
        assertEquals(Action.DELETE_PERMITTED_TERM, event.getAction());
        assertNotNull(event.getAutomodTerms());
        assertEquals(TermAction.REMOVE, event.getAutomodTerms().getAction());
        assertEquals(TermType.PERMITTED, event.getAutomodTerms().getList());
        assertEquals(Collections.singletonList("TriHard"), event.getAutomodTerms().getTerms());
        assertFalse(event.getAutomodTerms().isFromAutomod());
    }

    @Test
    void deserializeUnbanRequest() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"approve_unban_request\",\"followers\":null,\"slow\":null,\"vip\":null,\"unvip\":null,\"mod\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":null,\"unban_request\":{\"is_approved\":true,\"user_id\":\"53888434\",\"user_login\":\"ogprodigy\",\"user_name\":\"OGprodigy\",\"moderator_message\":\"welcome back\"}}",
            ChannelModerateEvent.class
        );
        assertEquals(Action.APPROVE_UNBAN_REQUEST, event.getAction());
        assertNotNull(event.getUnbanRequest());
        assertTrue(event.getUnbanRequest().isApproved());
        assertEquals("OGprodigy", event.getUnbanRequest().getUserName());
        assertEquals("welcome back", event.getUnbanRequest().getModeratorMessage());
    }

    @Test
    void deserializeSharedTimeout() {
        ChannelModerateEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"423374343\",\"broadcaster_user_login\":\"glowillig\",\"broadcaster_user_name\":\"glowillig\",\"source_broadcaster_user_id\":\"41292030\",\"source_broadcaster_user_login\":\"adflynn404\",\"source_broadcaster_user_name\":\"adflynn404\",\"moderator_user_id\":\"424596340\",\"moderator_user_login\":\"quotrok\",\"moderator_user_name\":\"quotrok\",\"action\":\"shared_chat_timeout\",\"followers\":null,\"slow\":null,\"vip\":null,\"unvip\":null,\"warn\":null,\"unmod\":null,\"ban\":null,\"unban\":null,\"timeout\":null,\"untimeout\":null,\"raid\":null,\"unraid\":null,\"delete\":null,\"automod_terms\":null,\"unban_request\":null,\"shared_chat_ban\":null,\"shared_chat_unban\":null,\"shared_chat_timeout\":{\"user_id\":\"141981764\",\"user_login\":\"twitchdev\",\"user_name\":\"TwitchDev\",\"reason\":\"Has never seen the Harry Potter films.\",\"expires_at\":\"2022-03-15T02:00:28Z\"},\"shared_chat_untimeout\":null,\"shared_chat_delete\":null}",
            ChannelModerateEvent.class
        );
        assertEquals(Action.SHARED_CHAT_TIMEOUT, event.getAction());
        assertNotNull(event.getSharedChatTimeout());
        assertEquals("423374343", event.getBroadcasterUserId());
        assertEquals("41292030", event.getSourceBroadcasterUserId());
        assertEquals("424596340", event.getModeratorUserId());
        assertEquals("141981764", event.getSharedChatTimeout().getUserId());
    }

}
