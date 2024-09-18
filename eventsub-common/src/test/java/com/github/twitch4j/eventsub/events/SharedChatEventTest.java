package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.domain.chat.MessageType;
import com.github.twitch4j.eventsub.domain.chat.NoticeType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unittest")
class SharedChatEventTest {

    @Test
    void deserializeBegin() {
        ChannelSharedChatBeginEvent e = TypeConvert.jsonToObject(
            "{\"session_id\":\"2b64a92a-dbb8-424e-b1c3-304423ba1b6f\",\"broadcaster_user_id\":\"1971641\",\"broadcaster_user_login\":\"streamer\",\"broadcaster_user_name\":\"streamer\",\"host_broadcaster_user_id\":\"1971641\",\"host_broadcaster_user_login\":\"streamer\",\"host_broadcaster_user_name\":\"streamer\",\"participants\":[{\"broadcaster_user_id\":\"1971641\",\"broadcaster_user_name\":\"streamer\",\"broadcaster_user_login\":\"streamer\"},{\"broadcaster_user_id\":\"112233\",\"broadcaster_user_name\":\"streamer33\",\"broadcaster_user_login\":\"streamer33\"}]}",
            ChannelSharedChatBeginEvent.class
        );
        assertEquals("2b64a92a-dbb8-424e-b1c3-304423ba1b6f", e.getSessionId());
        assertEquals("1971641", e.getBroadcasterUserId());
        assertEquals("streamer", e.getBroadcasterUserLogin());
        assertEquals("streamer", e.getBroadcasterUserName());
        assertEquals("1971641", e.getHostBroadcasterUserId());
        assertEquals("streamer", e.getHostBroadcasterUserLogin());
        assertEquals("streamer", e.getHostBroadcasterUserName());
        assertNotNull(e.getParticipants());
        assertEquals(2, e.getParticipants().size());
        assertEquals("1971641", e.getParticipants().get(0).getBroadcasterUserId());
        assertEquals("112233", e.getParticipants().get(1).getBroadcasterUserId());
    }

    @Test
    void deserializeUpdate() {
        ChannelSharedChatUpdateEvent e = TypeConvert.jsonToObject(
            "{\"session_id\":\"2b64a92a-dbb8-424e-b1c3-304423ba1b6f\",\"broadcaster_user_id\":\"1971641\",\"broadcaster_user_login\":\"streamer\",\"broadcaster_user_name\":\"streamer\",\"host_broadcaster_user_id\":\"1971641\",\"host_broadcaster_user_login\":\"streamer\",\"host_broadcaster_user_name\":\"streamer\",\"participants\":[{\"broadcaster_user_id\":\"1971641\",\"broadcaster_user_name\":\"streamer\",\"broadcaster_user_login\":\"streamer\"},{\"broadcaster_user_id\":\"112233\",\"broadcaster_user_name\":\"streamer33\",\"broadcaster_user_login\":\"streamer33\"},{\"broadcaster_user_id\":\"332211\",\"broadcaster_user_name\":\"streamer11\",\"broadcaster_user_login\":\"streamer11\"}]}",
            ChannelSharedChatUpdateEvent.class
        );
        assertEquals("2b64a92a-dbb8-424e-b1c3-304423ba1b6f", e.getSessionId());
        assertEquals("1971641", e.getBroadcasterUserId());
        assertEquals("1971641", e.getHostBroadcasterUserId());
        assertNotNull(e.getParticipants());
        assertEquals(3, e.getParticipants().size());
        assertEquals("1971641", e.getParticipants().get(0).getBroadcasterUserId());
        assertEquals("112233", e.getParticipants().get(1).getBroadcasterUserId());
        assertEquals("332211", e.getParticipants().get(2).getBroadcasterUserId());
    }

    @Test
    void deserializeEnd() {
        ChannelSharedChatEndEvent e = TypeConvert.jsonToObject(
            "{\"session_id\":\"2b64a92a-dbb8-424e-b1c3-304423ba1b6f\",\"broadcaster_user_id\":\"1971641\",\"broadcaster_user_login\":\"streamer\",\"broadcaster_user_name\":\"streamer\",\"host_broadcaster_user_id\":\"1971641\",\"host_broadcaster_user_login\":\"streamer\",\"host_broadcaster_user_name\":\"streamer\"}",
            ChannelSharedChatEndEvent.class
        );
        assertEquals("2b64a92a-dbb8-424e-b1c3-304423ba1b6f", e.getSessionId());
        assertEquals("1971641", e.getBroadcasterUserId());
        assertEquals("1971641", e.getHostBroadcasterUserId());
    }

    @Test
    void deserializeMessage() {
        ChannelChatMessageEvent e = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1025597036\",\"broadcaster_user_login\":\"shared_chat_test_02\",\"broadcaster_user_name\":\"shared_chat_test_02\",\"source_broadcaster_user_id\":\"1025594235\",\"source_broadcaster_user_login\":\"shared_chat_test_01\",\"source_broadcaster_user_name\":\"shared_chat_test_01\",\"chatter_user_id\":\"53888434\",\"chatter_user_login\":\"ogprodigy\",\"chatter_user_name\":\"OGprodigy\",\"message_id\":\"e6326d81-c456-466c-9518-93626ad15982\",\"source_message_id\":\"493a96bf-0384-4e96-9a7e-b105e8779601\",\"message\":{\"text\":\"hi from channel 1\",\"fragments\":[{\"type\":\"text\",\"text\":\"hi from channel 1\",\"cheermote\":null,\"emote\":null,\"mention\":null}]},\"color\":\"#00FF7F\",\"badges\":[{\"set_id\":\"glitchcon2020\",\"id\":\"1\",\"info\":\"\"}],\"source_badges\":[{\"set_id\":\"glitchcon2020\",\"id\":\"1\",\"info\":\"\"}],\"message_type\":\"text\",\"cheer\":null,\"reply\":null,\"channel_points_custom_reward_id\":null,\"channel_points_animation_id\":null}",
            ChannelChatMessageEvent.class
        );
        assertEquals("1025597036", e.getBroadcasterUserId());
        assertEquals("shared_chat_test_02", e.getBroadcasterUserLogin());
        assertEquals("1025594235", e.getSourceBroadcasterUserId());
        assertEquals("shared_chat_test_01", e.getSourceBroadcasterUserLogin());
        assertEquals("53888434", e.getChatterUserId());
        assertEquals("OGprodigy", e.getChatterUserName());
        assertEquals("e6326d81-c456-466c-9518-93626ad15982", e.getMessageId());
        assertEquals("493a96bf-0384-4e96-9a7e-b105e8779601", e.getSourceMessageId());
        assertEquals("hi from channel 1", e.getMessage().getText());
        assertEquals(MessageType.TEXT, e.getMessageType());
        assertEquals(1, e.getBadges().size());
        assertEquals("glitchcon2020", e.getBadges().get(0).getSetId());
        assertEquals(e.getBadges(), e.getSourceBadges());
        assertNull(e.getReply());
    }

    @Test
    void deserializeReply() {
        ChannelChatMessageEvent e = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1025594235\",\"broadcaster_user_login\":\"shared_chat_test_01\",\"broadcaster_user_name\":\"shared_chat_test_01\",\"source_broadcaster_user_id\":\"1025597036\",\"source_broadcaster_user_login\":\"shared_chat_test_02\",\"source_broadcaster_user_name\":\"shared_chat_test_02\",\"chatter_user_id\":\"53888434\",\"chatter_user_login\":\"ogprodigy\",\"chatter_user_name\":\"OGprodigy\",\"message_id\":\"ea9143d6-1293-42b5-a65b-08e5c5a31b3b\",\"source_message_id\":\"c32693d6-0178-4770-995c-ce41f9f09e6d\",\"message\":{\"text\":\"@OGprodigy reply from channel 2\",\"fragments\":[{\"type\":\"mention\",\"text\":\"@OGprodigy\",\"cheermote\":null,\"emote\":null,\"mention\":{\"user_id\":\"53888434\",\"user_login\":\"ogprodigy\",\"user_name\":\"OGprodigy\"}},{\"type\":\"text\",\"text\":\" reply from channel 2\",\"cheermote\":null,\"emote\":null,\"mention\":null}]},\"color\":\"#00FF7F\",\"badges\":[{\"set_id\":\"glitchcon2020\",\"id\":\"1\",\"info\":\"\"}],\"source_badges\":[{\"set_id\":\"glitchcon2020\",\"id\":\"1\",\"info\":\"\"}],\"message_type\":\"text\",\"cheer\":null,\"reply\":{\"parent_message_id\":\"493a96bf-0384-4e96-9a7e-b105e8779601\",\"parent_message_body\":\"hi from channel 1\",\"parent_user_id\":\"53888434\",\"parent_user_name\":\"OGprodigy\",\"parent_user_login\":\"ogprodigy\",\"thread_message_id\":\"493a96bf-0384-4e96-9a7e-b105e8779601\",\"thread_user_id\":\"53888434\",\"thread_user_name\":\"OGprodigy\",\"thread_user_login\":\"ogprodigy\"},\"channel_points_custom_reward_id\":null,\"channel_points_animation_id\":null}",
            ChannelChatMessageEvent.class
        );
        assertEquals("ea9143d6-1293-42b5-a65b-08e5c5a31b3b", e.getMessageId());
        assertEquals("c32693d6-0178-4770-995c-ce41f9f09e6d", e.getSourceMessageId());
        assertEquals("@OGprodigy reply from channel 2", e.getMessage().getText());
        assertNotNull(e.getReply());
        assertEquals("493a96bf-0384-4e96-9a7e-b105e8779601", e.getReply().getParentMessageId());
        assertEquals("493a96bf-0384-4e96-9a7e-b105e8779601", e.getReply().getThreadMessageId());
        assertEquals("hi from channel 1", e.getReply().getParentMessageBody());
    }

    @Test
    void deserializeRaid() {
        ChannelChatNotificationEvent e = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"1025594235\",\"broadcaster_user_login\":\"shared_chat_test_01\",\"broadcaster_user_name\":\"shared_chat_test_01\",\"source_broadcaster_user_id\":\"1025597036\",\"source_broadcaster_user_login\":\"shared_chat_test_02\",\"source_broadcaster_user_name\":\"shared_chat_test_02\",\"chatter_user_id\":\"53888434\",\"chatter_user_login\":\"ogprodigy\",\"chatter_user_name\":\"OGprodigy\",\"chatter_is_anonymous\":false,\"color\":\"#00FF7F\",\"badges\":[{\"set_id\":\"glitchcon2020\",\"id\":\"1\",\"info\":\"\"}],\"source_badges\":[{\"set_id\":\"glitchcon2020\",\"id\":\"1\",\"info\":\"\"}],\"system_message\":\"1 raiders from OGprodigy have joined!\",\"message_id\":\"b2189d62-cc7a-4aae-ad82-7576febd3a66\",\"source_message_id\":\"bb799110-a247-46a5-9a46-4ed9e72d946c\",\"message\":{\"text\":\"\",\"fragments\":[]},\"notice_type\":\"shared_chat_raid\",\"sub\":null,\"resub\":null,\"sub_gift\":null,\"community_sub_gift\":null,\"gift_paid_upgrade\":null,\"prime_paid_upgrade\":null,\"pay_it_forward\":null,\"raid\":null,\"unraid\":null,\"announcement\":null,\"bits_badge_tier\":null,\"charity_donation\":null,\"shared_chat_sub\":null,\"shared_chat_resub\":null,\"shared_chat_sub_gift\":null,\"shared_chat_community_sub_gift\":null,\"shared_chat_gift_paid_upgrade\":null,\"shared_chat_prime_paid_upgrade\":null,\"shared_chat_pay_it_forward\":null,\"shared_chat_raid\":{\"user_id\":\"53888434\",\"user_name\":\"OGprodigy\",\"user_login\":\"ogprodigy\",\"viewer_count\":1,\"profile_image_url\":\"https://static-cdn.jtvnw.net/jtv_user_pictures/860c5a53-13fd-47d4-9d5a-05188071c17f-profile_image-300x300.png\"},\"shared_chat_announcement\":null}",
            ChannelChatNotificationEvent.class
        );
        assertEquals(NoticeType.SHARED_CHAT_RAID, e.getNoticeType());
        assertNotNull(e.getSharedChatRaid());
        assertEquals("53888434", e.getSharedChatRaid().getUserId());
        assertEquals("OGprodigy", e.getSharedChatRaid().getUserName());
        assertEquals(1, e.getSharedChatRaid().getViewerCount());
    }

}
