package com.github.twitch4j.chat.enums;

import com.github.twitch4j.chat.events.channel.ChannelNoticeEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// This class was largely auto-generated via:
// Array.from(tbody.children).slice(1).map(o => {
//     const el = $(o);
//     const name = el.find("td > code").text().toUpperCase();
//     const desc = escapeHtml(el.find("td:last").text());
//     return "/**\n * " + desc + "\n */ \n" + name;
// }).join(",\n\n")

/**
 * The documented possible values of {@link ChannelNoticeEvent#getMsgId()}.
 *
 * @see <a href="https://dev.twitch.tv/docs/irc/msg-id">Official Documentation</a>
 */
public enum NoticeTag {

    /**
     * &lt;user&gt; is already banned in this channel.
     */
    ALREADY_BANNED,

    /**
     * This room is not in emote-only mode.
     */
    ALREADY_EMOTE_ONLY_OFF,

    /**
     * This room is already in emote-only mode.
     */
    ALREADY_EMOTE_ONLY_ON,

    /**
     * This room is not in r9k mode.
     */
    ALREADY_R9K_OFF,

    /**
     * This room is already in r9k mode.
     */
    ALREADY_R9K_ON,

    /**
     * This room is not in subscribers-only mode.
     */
    ALREADY_SUBS_OFF,

    /**
     * This room is already in subscribers-only mode.
     */
    ALREADY_SUBS_ON,

    /**
     * You cannot ban admin &lt;user&gt;. Please email support@twitch.tv if an admin is being abusive.
     */
    BAD_BAN_ADMIN,

    /**
     * You cannot ban anonymous users.
     */
    BAD_BAN_ANON,

    /**
     * You cannot ban the broadcaster.
     */
    BAD_BAN_BROADCASTER,

    /**
     * You cannot ban global moderator &lt;user&gt;. Please email support@twitch.tv if a global moderator is being abusive.
     */
    @Deprecated
    BAD_BAN_GLOBAL_MOD,

    /**
     * You cannot ban moderator &lt;user&gt; unless you are the owner of this channel.
     */
    BAD_BAN_MOD,

    /**
     * You cannot ban yourself.
     */
    BAD_BAN_SELF,

    /**
     * You cannot ban a staff &lt;user&gt;. Please email support@twitch.tv if a staff member is being abusive.
     */
    BAD_BAN_STAFF,

    /**
     * Failed to start commercial.
     */
    BAD_COMMERCIAL_ERROR,

    /**
     * You cannot delete the broadcaster&#039;s messages.
     */
    BAD_DELETE_MESSAGE_BROADCASTER,

    /**
     * Failed to delete message.
     */
    @Unofficial
    BAD_DELETE_MESSAGE_ERROR,

    /**
     * You cannot delete messages from another moderator &lt;user&gt;.
     */
    BAD_DELETE_MESSAGE_MOD,

    /**
     * There was a problem hosting &lt;channel&gt;. Please try again in a minute.
     */
    BAD_HOST_ERROR,

    /**
     * This channel is already hosting &lt;channel&gt;.
     */
    BAD_HOST_HOSTING,

    /**
     * Host target cannot be changed more than &lt;number&gt; times every half hour.
     */
    BAD_HOST_RATE_EXCEEDED,

    /**
     * This channel is unable to be hosted.
     */
    BAD_HOST_REJECTED,

    /**
     * A channel cannot host itself.
     */
    BAD_HOST_SELF,

    /**
     * Sorry, /marker is not available through this client.
     */
    BAD_MARKER_CLIENT,

    /**
     * &lt;user&gt; is banned in this channel. You must unban this user before granting mod status.
     */
    BAD_MOD_BANNED,

    /**
     * &lt;user&gt; is already a moderator of this channel.
     */
    BAD_MOD_MOD,

    /**
     * You cannot set slow delay to more than &lt;number&gt; seconds.
     */
    BAD_SLOW_DURATION,

    /**
     * You cannot timeout admin &lt;user&gt;. Please email support@twitch.tv if an admin is being abusive.
     */
    BAD_TIMEOUT_ADMIN,

    /**
     * You cannot timeout anonymous users.
     */
    BAD_TIMEOUT_ANON,

    /**
     * You cannot timeout the broadcaster.
     */
    BAD_TIMEOUT_BROADCASTER,

    /**
     * You cannot time a user out for more than &lt;seconds&gt;.
     */
    BAD_TIMEOUT_DURATION,

    /**
     * You cannot timeout global moderator &lt;user&gt;. Please email support@twitch.tv if a global moderator is being abusive.
     */
    @Deprecated
    BAD_TIMEOUT_GLOBAL_MOD,

    /**
     * You cannot timeout moderator &lt;user&gt; unless you are the owner of this channel.
     */
    BAD_TIMEOUT_MOD,

    /**
     * You cannot timeout yourself.
     */
    BAD_TIMEOUT_SELF,

    /**
     * You cannot timeout staff &lt;user&gt;. Please email support@twitch.tv if a staff member is being abusive.
     */
    BAD_TIMEOUT_STAFF,

    /**
     * &lt;user&gt; is not banned from this channel.
     */
    BAD_UNBAN_NO_BAN,

    /**
     * There was a problem exiting host mode. Please try again in a minute.
     */
    BAD_UNHOST_ERROR,

    /**
     * &lt;user&gt; is not a moderator of this channel.
     */
    BAD_UNMOD_MOD,

    /**
     * &lt;user&gt; is not a VIP of this channel.
     */
    @Unofficial
    BAD_UNVIP_GRANTEE_NOT_VIP,

    /**
     * Unable to add VIP. Visit the Achievements page on your dashboard to learn how to unlock this feature.
     */
    @Unofficial
    BAD_VIP_ACHIEVEMENT_INCOMPLETE,

    /**
     * &lt;user&gt; is already a VIP of this channel.
     */
    @Unofficial
    BAD_VIP_GRANTEE_ALREADY_VIP,

    /**
     * &lt;user&gt; is banned in this channel. You must unban this user before granting VIP status.
     */
    @Unofficial
    BAD_VIP_GRANTEE_BANNED,

    /**
     * Unable to add VIP. Visit the Achievements page on your dashboard to learn how to unlock additional VIP slots.
     */
    @Unofficial
    BAD_VIP_MAX_VIPS_REACHED,

    /**
     * &lt;user&gt; is now banned from this channel.
     */
    BAN_SUCCESS,

    /**
     * Commands available to you in this room (use /help &lt;command&gt; for details): &lt;list of commands&gt;
     */
    CMDS_AVAILABLE,

    /**
     * Your color has been changed.
     */
    COLOR_CHANGED,

    /**
     * Initiating &lt;number&gt; second commercial break. Keep in mind that your stream is still live and not everyone will get a commercial.
     */
    COMMERCIAL_SUCCESS,

    /**
     * The message from &lt;user&gt; is now deleted.
     */
    DELETE_MESSAGE_SUCCESS,

    /**
     * This room is no longer in emote-only mode.
     */
    EMOTE_ONLY_OFF,

    /**
     * This room is now in emote-only mode.
     */
    EMOTE_ONLY_ON,

    /**
     * This room is no longer in followers-only mode. Note: The followers tags are broadcast to a channel when a moderator makes changes.
     */
    FOLLOWERS_OFF,

    /**
     * This room is now in &lt;duration&gt; followers-only mode. Examples: “This room is now in 2 week followers-only mode.” or “This room is now in 1 minute followers-only mode.”
     */
    FOLLOWERS_ON,

    /**
     * This room is now in followers-only mode.
     */
    FOLLOWERS_ONZERO,

    /**
     * Exited host mode.
     */
    HOST_OFF,

    /**
     * Now hosting &lt;channel&gt;.
     */
    HOST_ON,

    /**
     * &lt;user&gt; is now hosting you.
     */
    HOST_SUCCESS,

    /**
     * &lt;user&gt; is now hosting you for up to &lt;number&gt; viewers.
     */
    HOST_SUCCESS_VIEWERS,

    /**
     * &lt;channel&gt; has gone offline. Exiting host mode.
     */
    HOST_TARGET_WENT_OFFLINE,

    /**
     * &lt;number&gt; host commands remaining this half hour.
     */
    HOSTS_REMAINING,

    /**
     * Invalid username: &lt;user&gt;
     */
    INVALID_USER,

    /**
     * You have added &lt;user&gt; as a moderator of this channel.
     */
    MOD_SUCCESS,

    /**
     * You are permanently banned from talking in &lt;channel&gt;.
     */
    MSG_BANNED,

    /**
     * Your message was not sent because it contained too many characters that could not be processed. If you believe this is an error, rephrase and try again.
     */
    MSG_BAD_CHARACTERS,

    /**
     * Your message was not sent because your email address is banned from this channel.
     */
    @Unofficial
    MSG_BANNED_EMAIL_ALIAS,

    /**
     * Your message was not sent because your account is not in good standing in this channel.
     */
    MSG_CHANNEL_BLOCKED,

    /**
     * This channel has been suspended.
     */
    MSG_CHANNEL_SUSPENDED,

    /**
     * Your message was not sent because it is identical to the previous one you sent, less than 30 seconds ago.
     */
    MSG_DUPLICATE,

    /**
     * This room is in emote only mode. You can find your currently available emoticons using the smiley in the chat text area.
     */
    MSG_EMOTEONLY,

    /**
     * You must use Facebook Connect to send messages to this channel. You can see Facebook Connect in your Twitch settings under the connections tab.
     */
    MSG_FACEBOOK,

    /**
     * This room is in &lt;duration&gt; followers-only mode. Follow &lt;channel&gt; to join the community!Note: These msg_followers tags are kickbacks to a user who does not meet the criteria; that is, does not follow or has not followed long enough.
     */
    MSG_FOLLOWERSONLY,

    /**
     * This room is in &lt;duration1&gt; followers-only mode. You have been following for &lt;duration2&gt;. Continue following to chat!
     */
    MSG_FOLLOWERSONLY_FOLLOWED,

    /**
     * This room is in followers-only mode. Follow &lt;channel&gt; to join the community!
     */
    MSG_FOLLOWERSONLY_ZERO,

    /**
     * This room is in r9k mode and the message you attempted to send is not unique.
     */
    MSG_R9K,

    /**
     * Your message was not sent because you are sending messages too quickly.
     */
    MSG_RATELIMIT,

    /**
     * Hey! Your message is being checked by mods and has not been sent.
     */
    MSG_REJECTED,

    /**
     * Your message wasn&#039;t posted due to conflicts with the channel&#039;s moderation settings.
     */
    MSG_REJECTED_MANDATORY,

    /**
     * A verified phone number is required to chat in this channel. Please visit https://www.twitch.tv/settings/security to verify your phone number.
     */
    @Unofficial
    MSG_REQUIRES_VERIFIED_PHONE_NUMBER,

    /**
     * The room was not found.
     */
    MSG_ROOM_NOT_FOUND,

    /**
     * This room is in slow mode and you are sending messages too quickly. You will be able to talk again in &lt;number&gt; seconds.
     */
    MSG_SLOWMODE,

    /**
     * This room is in subscribers only mode. To talk, purchase a channel subscription at https://www.twitch.tv/products/&lt;broadcaster login name&gt;/ticket?ref=subscriber_only_mode_chat.
     */
    MSG_SUBSONLY,

    /**
     * Your account has been suspended.
     */
    MSG_SUSPENDED,

    /**
     * You are banned from talking in &lt;channel&gt; for &lt;number&gt; more seconds.
     */
    MSG_TIMEDOUT,

    /**
     * This room requires a verified email address to chat. Please verify your email at https://www.twitch.tv/settings/profile.
     */
    MSG_VERIFIED_EMAIL,

    /**
     * No help available.
     */
    NO_HELP,

    /**
     * There are no moderators of this channel.
     */
    NO_MODS,

    /**
     * No channel is currently being hosted.
     */
    NOT_HOSTING,

    /**
     * You don’t have permission to perform that action.
     */
    NO_PERMISSION,

    /**
     * This channel does not have any VIPs.
     */
    @Unofficial
    NO_VIPS,

    /**
     * This room is no longer in r9k mode.
     */
    R9K_OFF,

    /**
     * This room is now in r9k mode.
     */
    R9K_ON,

    /**
     * You already have a raid in progress.
     */
    RAID_ERROR_ALREADY_RAIDING,

    /**
     * You cannot raid this channel.
     */
    RAID_ERROR_FORBIDDEN,

    /**
     * A channel cannot raid itself.
     */
    RAID_ERROR_SELF,

    /**
     * Sorry, you have more viewers than the maximum currently supported by raids right now.
     */
    RAID_ERROR_TOO_MANY_VIEWERS,

    /**
     * There was a problem raiding &lt;channel&gt;. Please try again in a minute.
     */
    RAID_ERROR_UNEXPECTED,

    /**
     * This channel is intended for mature audiences.
     */
    RAID_NOTICE_MATURE,

    /**
     * This channel has follower or subscriber only chat.
     */
    RAID_NOTICE_RESTRICTED_CHAT,

    /**
     * The moderators of this channel are: &lt;list of users&gt;
     */
    ROOM_MODS,

    /**
     * This room is no longer in slow mode.
     */
    SLOW_OFF,

    /**
     * This room is now in slow mode. You may send messages every &lt;number&gt; seconds.
     */
    SLOW_ON,

    /**
     * This room is no longer in subscribers-only mode.
     */
    SUBS_OFF,

    /**
     * This room is now in subscribers-only mode.
     */
    SUBS_ON,

    /**
     * &lt;user&gt; is not timed out from this channel.
     */
    TIMEOUT_NO_TIMEOUT,

    /**
     * &lt;user&gt; has been timed out for &lt;duration&gt; seconds.
     */
    TIMEOUT_SUCCESS,

    /**
     * The community has closed channel &lt;channel&gt; due to Terms of Service violations.
     */
    TOS_BAN,

    /**
     * Only turbo users can specify an arbitrary hex color. Use one of the following instead: &lt;list of colors&gt;.
     */
    TURBO_ONLY_COLOR,

    /**
     * &lt;user&gt; is no longer banned from this channel.
     */
    UNBAN_SUCCESS,

    /**
     * You have removed &lt;user&gt; as a moderator of this channel.
     */
    UNMOD_SUCCESS,

    /**
     * You do not have an active raid.
     */
    UNRAID_ERROR_NO_ACTIVE_RAID,

    /**
     * There was a problem stopping the raid. Please try again in a minute.
     */
    UNRAID_ERROR_UNEXPECTED,

    /**
     * The raid has been cancelled.
     */
    UNRAID_SUCCESS,

    /**
     * Unrecognized command: &lt;command&gt;
     */
    UNRECOGNIZED_CMD,

    /**
     * The command &lt;command&gt; cannot be used in a chatroom.
     */
    UNSUPPORTED_CHATROOMS_CMD,

    /**
     * &lt;user&gt; is permanently banned. Use &quot;/unban&quot; to remove a ban.
     */
    UNTIMEOUT_BANNED,

    /**
     * &lt;user&gt; is no longer timed out in this channel.
     */
    UNTIMEOUT_SUCCESS,

    /**
     * You have removed &lt;user&gt; as a VIP of this channel.
     */
    @Unofficial
    UNVIP_SUCCESS,

    /**
     * Usage: “/ban &lt;username&gt; [reason]” Permanently prevent a user from chatting. Reason is optional and will be shown to the target and other moderators. Use “/unban” to remove a ban.
     */
    USAGE_BAN,

    /**
     * Usage: “/clear” Clear chat history for all users in this room.
     */
    USAGE_CLEAR,

    /**
     * Usage: “/color &lt;color&gt;” Change your username color. Color must be in hex (#000000) or one of the following: Blue, BlueViolet, CadetBlue, Chocolate, Coral, DodgerBlue, Firebrick, GoldenRod, Green, HotPink, OrangeRed, Red, SeaGreen, SpringGreen, YellowGreen.
     */
    USAGE_COLOR,

    /**
     * Usage: “/commercial [length]” Triggers a commercial. Length (optional) must be a positive number of seconds.
     */
    USAGE_COMMERCIAL,

    /**
     * Usage: “/disconnect” Reconnects to chat.
     */
    USAGE_DISCONNECT,

    /**
     * Usage: “/emoteonlyoff” Disables emote-only mode.
     */
    USAGE_EMOTE_ONLY_OFF,

    /**
     * Usage: “/emoteonly” Enables emote-only mode (only emoticons may be used in chat). Use /emoteonlyoff to disable.
     */
    USAGE_EMOTE_ONLY_ON,

    /**
     * Usage: “/followersoff” Disables followers-only mode.
     */
    USAGE_FOLLOWERS_OFF,

    /**
     * Usage: “/followers” Enables followers-only mode (only users who have followed for “duration” may chat). Examples: “30m”, “1 week”, “5 days 12 hours”. Must be less than 3 months.
     */
    USAGE_FOLLOWERS_ON,

    /**
     * Usage: “/help” Lists the commands available to you in this room.
     */
    USAGE_HELP,

    /**
     * Usage: “/host &lt;channel&gt;” Host another channel. Use “/unhost” to unset host mode.
     */
    USAGE_HOST,

    /**
     * Usage: “/marker &lt;optional comment&gt;” Adds a stream marker (with an optional comment, max 140 characters) at the current timestamp. You can use markers in the Highlighter for easier editing.
     */
    USAGE_MARKER,

    /**
     * Usage: “/me &lt;message&gt;” Send an “emote” message in the third person.
     */
    USAGE_ME,

    /**
     * Usage: “/mod &lt;username&gt;” Grant mod status to a user. Use “/mods” to list the moderators of this channel.
     */
    USAGE_MOD,

    /**
     * Usage: “/mods” Lists the moderators of this channel.
     */
    USAGE_MODS,

    /**
     * Usage: “/r9kbetaoff” Disables r9k mode.
     */
    USAGE_R9K_OFF,

    /**
     * Usage: “/r9kbeta” Enables r9k mode. Use “/r9kbetaoff“ to disable.
     */
    USAGE_R9K_ON,

    /**
     * Usage: “/raid &lt;channel&gt;” Raid another channel. Use “/unraid” to cancel the Raid.
     */
    USAGE_RAID,

    /**
     * Usage: “/slowoff” Disables slow mode.
     */
    USAGE_SLOW_OFF,

    /**
     * Usage: “/slow [duration]” Enables slow mode (limit how often users may send messages). Duration (optional, default=&lt;number&gt;) must be a positive integer number of seconds. Use “/slowoff” to disable.
     */
    USAGE_SLOW_ON,

    /**
     * Usage: “/subscribersoff” Disables subscribers-only mode.
     */
    USAGE_SUBS_OFF,

    /**
     * Usage: “/subscribers” Enables subscribers-only mode (only subscribers may chat in this channel). Use “/subscribersoff” to disable.
     */
    USAGE_SUBS_ON,

    /**
     * Usage: “/timeout &lt;username&gt; [duration][time unit] [reason]&quot;Temporarily prevent a user from chatting. Duration (optional, default=10 minutes) must be a positive integer; time unit (optional, default=s) must be one of s, m, h, d, w;
     * maximum duration is 2 weeks. Combinations like 1d2h are also allowed. Reason is optional and will be shown to the target user and other moderators. Use “untimeout” to remove a timeout.
     */
    USAGE_TIMEOUT,

    /**
     * Usage: “/unban &lt;username&gt;” Removes a ban on a user.
     */
    USAGE_UNBAN,

    /**
     * Usage: “/unhost” Stop hosting another channel.
     */
    USAGE_UNHOST,

    /**
     * Usage: “/unmod &lt;username&gt;” Revoke mod status from a user. Use “/mods” to list the moderators of this channel.
     */
    USAGE_UNMOD,

    /**
     * Usage: “/unraid” Cancel the Raid.
     */
    USAGE_UNRAID,

    /**
     * Usage: “/untimeout &lt;username&gt;” Removes a timeout on a user.
     */
    USAGE_UNTIMEOUT,

    /**
     * Usage: “/vip &lt;username&gt;” Grant VIP status to a user. Use “/vips” to list the moderators of this channel.
     */
    @Unofficial
    USAGE_VIP,

    /**
     * Usage: “/vips” Lists the VIPs of this channel.
     */
    @Unofficial
    USAGE_VIPS,

    /**
     * You have added &lt;user&gt; as a VIP of this channel.
     */
    @Unofficial
    VIP_SUCCESS,

    /**
     * The VIPs of this channel are: &lt;list of users&gt;.
     */
    @Unofficial
    VIPS_SUCCESS,

    /**
     * You have been banned from sending whispers.
     */
    WHISPER_BANNED,

    /**
     * That user has been banned from receiving whispers.
     */
    WHISPER_BANNED_RECIPIENT,

    /**
     * Usage: &lt;login&gt; &lt;message&gt;
     */
    WHISPER_INVALID_ARGS,

    /**
     * No user matching that login.
     */
    WHISPER_INVALID_LOGIN,

    /**
     * You cannot whisper to yourself.
     */
    WHISPER_INVALID_SELF,

    /**
     * You are sending whispers too fast. Try again in a minute.
     */
    WHISPER_LIMIT_PER_MIN,

    /**
     * You are sending whispers too fast. Try again in a second.
     */
    WHISPER_LIMIT_PER_SEC,

    /**
     * Your settings prevent you from sending this whisper.
     */
    WHISPER_RESTRICTED,

    /**
     * That user&#039;s settings prevent them from receiving this whisper.
     */
    WHISPER_RESTRICTED_RECIPIENT;

    private static final Map<String, NoticeTag> MAPPINGS = Arrays.stream(values()).collect(Collectors.toMap(NoticeTag::toString, Function.identity()));

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    /**
     * Attempts to parse the msgId to an enum value.
     *
     * @param msgId the lowercase msg_id from a NOTICE chat event.
     * @return the parsed NoticeTag or null if no match was found.
     */
    @Nullable
    public static NoticeTag parse(String msgId) {
        return MAPPINGS.get(msgId);
    }

}
