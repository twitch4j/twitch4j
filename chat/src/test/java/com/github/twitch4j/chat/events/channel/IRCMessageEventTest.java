package com.github.twitch4j.chat.events.channel;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.IRCEventHandler;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.util.EventManagerUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.twitch4j.chat.util.MessageParser.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
public class IRCMessageEventTest {

    @Test
    @DisplayName("Tests that CLEARCHAT is parsed by IRCMessageEvent")
    void parseChatClear() {
        IRCMessageEvent e = parse("@room-id=12345678;tmi-sent-ts=1642715756806 :tmi.twitch.tv CLEARCHAT #dallas");

        assertEquals("CLEARCHAT", e.getCommandType());
        assertEquals("dallas", e.getChannelName().orElse(null));
        assertEquals("12345678", e.getChannelId());
    }

    @Test
    @DisplayName("Tests that CLEARMSG is parsed by IRCMessageEvent")
    void parseMessageDeletion() {
        IRCMessageEvent e = parse("@login=foo;room-id=;target-msg-id=94e6c7ff-bf98-4faa-af5d-7ad633a158a9;tmi-sent-ts=1642720582342 :tmi.twitch.tv CLEARMSG #bar :what a great day");

        assertEquals("foo", e.getUserName());
        assertEquals("bar", e.getChannelName().orElse(null));
        assertEquals("CLEARMSG", e.getCommandType());
        assertEquals("what a great day", e.getMessage().orElse(null));
        assertEquals("94e6c7ff-bf98-4faa-af5d-7ad633a158a9", e.getTagValue("target-msg-id").orElse(null));
    }

    @Test
    @DisplayName("Tests that GLOBALUSERSTATE is parsed by IRCMessageEvent")
    void parseGlobalUserState() {
        IRCMessageEvent e = parse("@badge-info=subscriber/8;badges=subscriber/6;color=#0D4200;display-name=dallas;emote-sets=0,33,50,237,793,2126,3517,4578,5569,9400,10337,12239;turbo=0;user-id=12345678;user-type=admin " +
            ":tmi.twitch.tv GLOBALUSERSTATE");

        assertEquals("GLOBALUSERSTATE", e.getCommandType());
        assertEquals("12345678", e.getUserId());
        assertEquals("dallas", e.getTagValue("display-name").orElse(null));
        assertEquals("dallas", e.getUserName());
        assertEquals("0,33,50,237,793,2126,3517,4578,5569,9400,10337,12239", e.getTagValue("emote-sets").orElse(null));
    }

    @Test
    @DisplayName("Test that normal messages are parsed by IRCMessageEvent")
    void parseMessage() {
        IRCMessageEvent e = parse("@badge-info=;badges=broadcaster/1;client-nonce=459e3142897c7a22b7d275178f2259e0;color=#0000FF;display-name=lovingt3s;emote-only=1;emotes=62835:0-10;first-msg=0;flags=;" +
            "id=885196de-cb67-427a-baa8-82f9b0fcd05f;mod=0;room-id=713936733;subscriber=0;tmi-sent-ts=1643904084794;turbo=0;user-id=713936733;user-type= " +
            ":lovingt3s!lovingt3s@lovingt3s.tmi.twitch.tv PRIVMSG #lovingt3s :bleedPurple");

        assertEquals("bleedPurple", e.getMessage().orElse(null));
        assertEquals("lovingt3s", e.getChannelName().orElse(null));
        assertEquals("713936733", e.getChannelId());
        assertEquals("713936733", e.getUserId());
        assertEquals("lovingt3s", e.getUserName());
        assertEquals("PRIVMSG", e.getCommandType());
        assertTrue(e.getClientPermissions().contains(CommandPermission.BROADCASTER));
        assertEquals("885196de-cb67-427a-baa8-82f9b0fcd05f", e.getMessageId().orElse(null));
        assertEquals("459e3142897c7a22b7d275178f2259e0", e.getNonce().orElse(null));
        assertEquals("62835:0-10", e.getTagValue("emotes").orElse(null));
    }

    @Test
    @DisplayName("Tests that NOTICE is parsed by IRCMessageEvent")
    void parseNotice() {
        IRCMessageEvent e = parse("@msg-id=delete_message_success :tmi.twitch.tv NOTICE #bar :The message from foo is now deleted.");

        assertEquals("NOTICE", e.getCommandType());
        assertEquals("bar", e.getChannelName().orElse(null));
        assertEquals("delete_message_success", e.getRawTagString("msg-id"));
        assertEquals("The message from foo is now deleted.", e.getMessage().orElse(null));
    }

    @Test
    @DisplayName("Tests that RECONNECT is parsed by IRCMessageEvent")
    void parseReconnect() {
        IRCMessageEvent e = parse(":tmi.twitch.tv RECONNECT");
        assertEquals("RECONNECT", e.getCommandType());
    }

    @Test
    @DisplayName("Tests that ROOMSTATE is parsed by IRCMessageEvent")
    void parseRoomState() {
        IRCMessageEvent e = parse("@emote-only=0;followers-only=-1;r9k=0;rituals=0;room-id=12345678;slow=0;subs-only=0 :tmi.twitch.tv ROOMSTATE #bar");
        assertEquals("ROOMSTATE", e.getCommandType());
        assertEquals("bar", e.getChannelName().orElse(null));
        assertEquals("12345678", e.getChannelId());
        assertEquals("0", e.getRawTagString("emote-only"));
        assertEquals("-1", e.getRawTagString("followers-only"));
    }

    @Test
    @DisplayName("Tests that USERNOTICE is parsed by IRCMessageEvent")
    void parseUserNotice() {
        IRCMessageEvent e = parse("@badge-info=;badges=staff/1,premium/1;color=#0000FF;display-name=TWW2;emotes=;id=e9176cd8-5e22-4684-ad40-ce53c2561c5e;login=tww2;mod=0;msg-id=subgift;" +
            "msg-param-months=1;msg-param-recipient-display-name=Mr_Woodchuck;msg-param-recipient-id=55554444;msg-param-recipient-name=mr_woodchuck;msg-param-sub-plan-name=House\\sof\\sNyoro~n;msg-param-sub-plan=1000;" +
            "room-id=12345678;subscriber=0;system-msg=TWW2\\sgifted\\sa\\sTier\\s1\\ssub\\sto\\sMr_Woodchuck!;tmi-sent-ts=1521159445153;turbo=0;user-id=87654321;user-type=staff :tmi.twitch.tv USERNOTICE #forstycup");

        assertEquals("USERNOTICE", e.getCommandType());
        assertEquals("12345678", e.getChannelId());
        assertEquals("forstycup", e.getChannelName().orElse(null));
        assertEquals("87654321", e.getUserId());
        assertEquals("TWW2 gifted a Tier 1 sub to Mr_Woodchuck!", e.getTagValue("system-msg").orElse(null));
        assertEquals("TWW2", e.getTagValue("display-name").orElse(null));
        assertEquals("tww2", e.getUserName());
        assertEquals("subgift", e.getTagValue("msg-id").orElse(null));
    }

    @Test
    @DisplayName("Tests that USERSTATE is parsed by IRCMessageEevent")
    void parseUserState() {
        IRCMessageEvent e = parse("@badge-info=;badges=staff/1;color=#0D4200;display-name=ronni;emote-sets=0,33,50,237,793,2126,3517,4578,5569,9400,10337,12239;mod=1;subscriber=1;turbo=1;user-type=staff " +
            ":tmi.twitch.tv USERSTATE #dallas");

        assertEquals("USERSTATE", e.getCommandType());
        assertEquals("dallas", e.getChannelName().orElse(null));
        assertEquals("ronni", e.getUserName());
        assertEquals("0,33,50,237,793,2126,3517,4578,5569,9400,10337,12239", e.getTagValue("emote-sets").orElse(null));
        assertTrue(e.getClientPermissions().contains(CommandPermission.TWITCHSTAFF));
    }

    @Test
    @DisplayName("Test that whispers are parsed by IRCMessageEvent")
    void parseWhisper() {
        IRCMessageEvent e = parse("@badges=;color=;display-name=HexaFice;emotes=;message-id=103;thread-id=142621956_149223493;turbo=0;user-id=142621956;user-type= " +
            ":hexafice!hexafice@hexafice.tmi.twitch.tv WHISPER twitch4j :test 123");

        assertEquals("test 123", e.getMessage().orElse(null));
        assertEquals("WHISPER", e.getCommandType());
        assertEquals("142621956", e.getUserId());
        assertEquals("hexafice", e.getUserName());
        assertEquals("HexaFice", e.getTagValue("display-name").orElse(null));
        assertEquals("twitch4j", e.getChannelName().orElse(null));
        assertTrue(e.getBadges() == null || e.getBadges().isEmpty());
        assertTrue(e.getBadgeInfo() == null || e.getBadgeInfo().isEmpty());
    }

    @Test
    @DisplayName("Test that 353 NAMES response is parsed by IRCMessageEvent")
    void parseNamesResponse() {
        IRCMessageEvent e = parse(":justinfan77645.tmi.twitch.tv 353 justinfan77645 = #pajlada :vissb ogprodigy supabridge zneix suslada beatz pajbot");

        assertEquals("353", e.getCommandType());
        assertEquals("pajlada", e.getChannelName().orElse(null));
        assertEquals("vissb ogprodigy supabridge zneix suslada beatz pajbot", e.getMessage().orElse(null));
        assertTrue(e.getClientName().isPresent());
        assertTrue(e.getBadges().isEmpty());
        assertTrue(e.getBadgeInfo().isEmpty());
    }

    @Test
    @DisplayName("Test that JOIN is parsed by IRCMessageEvent")
    void parseJoin() {
        IRCMessageEvent e = parse(":ogprodigy!ogprodigy@ogprodigy.tmi.twitch.tv JOIN #twitchdev");
        assertEquals("JOIN", e.getCommandType());
        assertEquals("twitchdev", e.getChannelName().orElse(null));
        assertEquals("ogprodigy", e.getUserName());
    }

    @Test
    @DisplayName("Test that PART is parsed by IRCMessageEvent")
    void parsePart() {
        IRCMessageEvent e = parse(":ogprodigy!ogprodigy@ogprodigy.tmi.twitch.tv PART #twitchdev");
        assertEquals("PART", e.getCommandType());
        assertEquals("twitchdev", e.getChannelName().orElse(null));
        assertEquals("ogprodigy", e.getUserName());
    }

    @Test
    @DisplayName("Test that watch-streak viewermilestone USERNOTICE is parsed by IRCMessageEvent and sub-event")
    @Unofficial
    void parseMilestone() {
        IRCMessageEvent rawEvent = parse("@msg-param-id=11439a09-3c9f-4701-9552-bba21f2028d2;" +
            "rm-received-ts=1700717888176;room-id=39842292;badge-info=subscriber/3;msg-param-category=watch-streak;" +
            "badges=subscriber/3;msg-param-value=7;subscriber=1;vip=0;user-type=;user-id=531667091;flags=;" +
            "msg-id=viewermilestone;display-name=Baughbby;login=baughbby;msg-param-copoReward=450;emotes=;" +
            "color=#DAA520;tmi-sent-ts=1700717888068;mod=0;system-msg=Baughbby\\swatched\\s7\\sconsecutive\\sstreams" +
            "\\sthis\\smonth\\sand\\ssparked\\sa\\swatch\\sstreak!;id=107473b2-01c2-49a5-9fe5-a4d7ffe54e5f " +
            ":tmi.twitch.tv USERNOTICE #nandre :ive done it");
        assertNotNull(rawEvent);
        ViewerMilestoneEvent event = new ViewerMilestoneEvent(rawEvent);
        assertEquals("531667091", event.getUser().getId());
        assertEquals("39842292", event.getChannel().getId());
        assertEquals("11439a09-3c9f-4701-9552-bba21f2028d2", event.getMilestoneUniqueId());
        assertEquals("watch-streak", event.getMilestoneCategory());
        assertEquals(7, event.parseValue().orElse(-1));
        assertEquals(450, event.getEarnedChannelPoints());
        assertEquals("ive done it", event.getUserMessage());
    }

    @Test
    @DisplayName("Test that stream together shared chat PRIVMSG is parsed by IRCMessageEvnet and sub-event")
    void parseSharedChatMessage() {
        IRCMessageEvent raw = parse("@badge-info=;badges=glitchcon2020/1;color=#00FF7F;display-name=OGprodigy;emotes=;flags=;id=e16cc51a-7fce-4a92-8387-4ac8b7794ee7;mod=0;reply-parent-display-name=OGprodigy;reply-parent-msg-body=@Alca\\soh\\sur\\sright;reply-parent-msg-id=3c231d98-fc47-44a0-81db-be3820102d79;reply-parent-user-id=53888434;reply-parent-user-login=ogprodigy;reply-thread-parent-display-name=Alca;reply-thread-parent-msg-id=c449d446-cd01-4460-b890-ad935f32b92a;reply-thread-parent-user-id=7676884;reply-thread-parent-user-login=alca;room-id=1025594235;source-badge-info=;source-badges=glitchcon2020/1;source-id=0b1e60f3-5990-4b26-94e2-885231614e07;source-room-id=1025597036;subscriber=0;tmi-sent-ts=1726007886662;turbo=0;user-id=53888434;user-type= " +
            ":ogprodigy!ogprodigy@ogprodigy.tmi.twitch.tv PRIVMSG #shared_chat_test_01 :@OGprodigy test3");
        assertNotNull(raw);
        assertEquals("e16cc51a-7fce-4a92-8387-4ac8b7794ee7", raw.getMessageId().orElse(null));
        assertEquals("7676884", raw.getRawTagString("reply-thread-parent-user-id"));

        ChannelMessageEvent e = new ChannelMessageEvent(raw.getChannel(), raw, raw.getUser(), raw.getMessage().orElse(null));
        assertTrue(e.isMirrored());
        assertFalse(e.getSourceNoticeType().isPresent());
        assertEquals("@OGprodigy test3", e.getMessage());
        assertEquals("ogprodigy", e.getUser().getName());
        assertEquals("53888434", e.getUser().getId());
        assertEquals("shared_chat_test_01", e.getChannel().getName());
        assertEquals("1025594235", e.getChannel().getId());
        assertEquals("1025597036", e.getSourceChannelId().orElse(null));
        assertEquals("0b1e60f3-5990-4b26-94e2-885231614e07", e.getSourceMessageId().orElse(null));
        assertEquals("@Alca oh ur right", e.getReplyInfo().getMessageBody());
        assertEquals("c449d446-cd01-4460-b890-ad935f32b92a", e.getReplyInfo().getThreadMessageId());
        assertEquals("1", e.getSourceBadges().get().get("glitchcon2020"));
        Map<String, String> badgeInfo = e.getSourceBadgeInfo().orElse(null);
        assertTrue(badgeInfo == null || badgeInfo.isEmpty());
    }

    @Test
    @DisplayName("Test that stream together shared chat USERNOTICE is parsed by IRCMessageEvnet and sub-event")
    void parseSharedChatNotification() {
        IRCMessageEvent raw = parse("@badge-info=;badges=staff/1,raging-wolf-helm/1;color=#DAA520;display-name=lahoooo;emotes=;flags=;id=01cd601f-bc3f-49d5-ab4b-136fa9d6ec22;login=lahoooo;mod=0;msg-id=sharedchatnotice;msg-param-color=PRIMARY;room-id=1025597036;source-badge-info=;source-badges=staff/1,moderator/1,bits-leader/1;source-id=4083dadc-9f20-40f9-ba92-949ebf6bc294;source-msg-id=announcement;source-room-id=1025594235;subscriber=0;system-msg=;tmi-sent-ts=1726118378465;user-id=612865661;user-type=staff;vip=0 " +
            ":tmi.twitch.tv USERNOTICE #shared_chat_test_02 :hi this is an announcement from 1");
        assertNotNull(raw);

        EventManager eventManager = EventManagerUtils.initializeEventManager(SimpleEventHandler.class);
        new IRCEventHandler(eventManager);

        List<ModAnnouncementEvent> events = new ArrayList<>(1);
        eventManager.onEvent(ModAnnouncementEvent.class, events::add);

        eventManager.publish(raw);

        assertEquals(1, events.size());
        ModAnnouncementEvent e = events.get(0);
        assertTrue(e.isMirrored());
        assertEquals("shared_chat_test_02", e.getChannel().getName());
        assertEquals("1025597036", e.getChannel().getId());
        assertEquals("1025594235", e.getSourceChannelId().orElse(null));
        assertEquals("hi this is an announcement from 1", e.getMessage());
        assertEquals("lahoooo", e.getAnnouncer().getName());
        assertEquals("612865661", e.getAnnouncer().getId());
        assertFalse(e.getMessageEvent().getBadges().containsKey("moderator"));
        Map<String, String> sourceBadges = e.getSourceBadges().orElse(null);
        assertTrue(sourceBadges != null && sourceBadges.containsKey("moderator"));
    }

}
