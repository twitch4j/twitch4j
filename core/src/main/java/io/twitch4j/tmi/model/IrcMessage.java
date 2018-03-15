/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.twitch4j.tmi.model;

import io.twitch4j.ITwitchClient;
import io.twitch4j.api.kraken.model.Subscription;
import io.twitch4j.events.Event;
import io.twitch4j.events.tmi.*;
import io.twitch4j.tmi.IMessageInterface;
import io.twitch4j.tmi.IUser;
import io.twitch4j.tmi.channel.IChannel;
import io.twitch4j.tmi.model.tags.Badge;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IRC Parser.
 *
 * @author Werner [https://github.com/3ventic]
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @see <a href = "https://github.com/3ventic/TwitchChatSharp/blob/master/TwitchChatSharp/IrcMessage.cs#L201">https://github.com/3ventic/TwitchChatSharp/blob/master/TwitchChatSharp/IrcMessage.cs#L201</a>
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class IrcMessage {
    /**
     * IRC Command
     */
    private final IrcCommand command;

    /**
     * IRC Parameters e.g. <code>#&lt;channel&gt;</code>
     */
    private final String parameters;

    /**
     * IRC Message
     */
    private final String message;

    /**
     * IRC Host Mask which contains username. <code>&lt;username&gt;!&lt;username&gt;@&lt;hostname&gt;</code>
     */
    private final String hostmask;

    /**
     * IRC Tags V3
     */
    private final JsonObject tags;

    /**
     * Parsing Raw Message
     *
     * @param rawMessage one line raw message
     * @return Parsed message into {@link IrcMessage} class
     */
    public static IrcMessage parseMessage(String rawMessage) {

        Map<String, String> tags = new HashMap<>();

        ParserState state = ParserState.STATE_NONE;
        int[] starts = {0, 0, 0, 0, 0, 0};
        int[] lens = {0, 0, 0, 0, 0, 0};
        for (int i = 0; i < rawMessage.length(); ++i) {
            lens[state.ordinal()] = i - starts[state.ordinal()] - 1;
            if (state.equals(ParserState.STATE_NONE) && rawMessage.startsWith("@")) {
                state = ParserState.STATE_V3;
                starts[state.ordinal()] = ++i;

                int start = i;
                String key = null;
                for (; i < rawMessage.length(); ++i) {
                    if (rawMessage.toCharArray()[i] == '=') {
                        key = rawMessage.substring(start, i - start);
                        start = i + 1;
                    } else if (rawMessage.toCharArray()[i] == ';') {
                        if (key == null)
                            tags.put(rawMessage.substring(start, i - start), "1");
                        else
                            tags.put(key, rawMessage.substring(start, i - start));
                        start = i + 1;
                    } else if (rawMessage.toCharArray()[i] == ' ') {
                        if (key == null)
                            tags.put(rawMessage.substring(start, i - start), "1");
                        else
                            tags.put(key, rawMessage.substring(start, i - start));
                        break;
                    }
                }
            } else if (state.ordinal() < ParserState.STATE_PREFIX.ordinal() && rawMessage.toCharArray()[i] == ':') {
                state = ParserState.STATE_PREFIX;
                starts[state.ordinal()] = ++i;
            } else if (state.ordinal() < ParserState.STATE_COMMAND.ordinal()) {
                state = ParserState.STATE_COMMAND;
                starts[state.ordinal()] = ++i;
            } else if (state.ordinal() < ParserState.STATE_TRAILING.ordinal() && rawMessage.toCharArray()[i] == ':') {
                state = ParserState.STATE_TRAILING;
                starts[state.ordinal()] = ++i;
                break;
            } else if (state.equals(ParserState.STATE_COMMAND)) {
                state = ParserState.STATE_PARAM;
                starts[state.ordinal()] = i;
            }
            while (i < rawMessage.length() && rawMessage.toCharArray()[i] != ' ') {
                ++i;
            }
        }

        lens[state.ordinal()] = rawMessage.length() - starts[state.ordinal()];
        String cmd = rawMessage.substring(starts[ParserState.STATE_COMMAND.ordinal()], lens[ParserState.STATE_COMMAND.ordinal()]);

        IrcCommand command = IrcCommand.UNKNOWN;
        switch (cmd) {
            case "PRIVMSG":
                command = IrcCommand.PRIV_MSG;
                break;
            case "NOTICE":
                command = IrcCommand.NOTICE;
                break;
            case "PING":
                command = IrcCommand.PING;
                break;
            case "PONG":
                command = IrcCommand.PONG;
                break;
            case "HOSTTARGET":
                command = IrcCommand.HOST_TARGET;
                break;
            case "CLEARCHAT":
                command = IrcCommand.CLEAR_CHAT;
                break;
            case "USERSTATE":
                command = IrcCommand.USER_STATE;
                break;
            case "GLOBALUSERSTATE":
                command = IrcCommand.GLOBAL_USER_STATE;
                break;
            case "NICK":
                command = IrcCommand.NICK;
                break;
            case "JOIN":
                command = IrcCommand.JOIN;
                break;
            case "PART":
                command = IrcCommand.PART;
                break;
            case "PASS":
                command = IrcCommand.PASS;
                break;
            case "CAP":
                command = IrcCommand.CAP;
                break;
            case "001":
                command = IrcCommand.RPL_001;
                break;
            case "002":
                command = IrcCommand.RPL_002;
                break;
            case "003":
                command = IrcCommand.RPL_003;
                break;
            case "004":
                command = IrcCommand.RPL_004;
                break;
            case "353":
                command = IrcCommand.RPL_353;
                break;
            case "366":
                command = IrcCommand.RPL_366;
                break;
            case "372":
                command = IrcCommand.RPL_372;
                break;
            case "375":
                command = IrcCommand.RPL_375;
                break;
            case "376":
                command = IrcCommand.RPL_376;
                break;
            case "WHISPER":
                command = IrcCommand.WHISPER;
                break;
            case "SERVERCHANGE":
                command = IrcCommand.SERVER_CHANGE;
                break;
            case "RECONNECT":
                command = IrcCommand.RECONNECT;
                break;
            case "ROOMSTATE":
                command = IrcCommand.ROOM_STATE;
                break;
            case "USERNOTICE":
                command = IrcCommand.USER_NOTICE;
                break;
        }

        String parameters = rawMessage.substring(starts[ParserState.STATE_PARAM.ordinal()], lens[ParserState.STATE_PARAM.ordinal()]);
        String message = rawMessage.substring(starts[ParserState.STATE_TRAILING.ordinal()], lens[ParserState.STATE_TRAILING.ordinal()]);
        String hostmask = rawMessage.substring(starts[ParserState.STATE_PREFIX.ordinal()], lens[ParserState.STATE_PREFIX.ordinal()]);
        return new IrcMessage(command, parameters, message, hostmask, Collections.unmodifiableMap(tags));
    }

    /**
     * Parse the message and dispatch the corresponding event
     *
     * @param message the IRC Message
     * @param client  client
     */
    public static void parseAndDispatchEvent(IrcMessage message, ITwitchClient client) {

        client.getDispatcher().dispatch(new IrcEvent(message));

        Event event = null;

        IChannel channel = StringUtils.startsWith(message.getParameters(), "#") && !StringUtils.startsWith(message.getParameters(), "#chatrooms") ?
                client.getMessageInterface().getChannel(message.getParameters().substring(1, message.getParameters().indexOf(" "))) : null;
        boolean action = StringUtils.contains(message.getMessage(), "ACTION");
        String msg = message.getMessage().replace("\u0001", "").replace("ACTION ", "");
        Map<String, String> tags = message.getTags();
        String preUser = getUserFromHostmask(message.getHostmask());
        IUser user = StringUtils.isNotBlank(preUser) ? client.getMessageInterface().createPrivateMessage(preUser) : null;

        switch (message.getCommand()) {
            case JOIN:
                event = new JoinChannelEvent(channel, user);
                break;
            case PART:
                event = new PartChannelEvent(channel, user);
                break;
            case PRIV_MSG:
                // dispatching message event to customize own message event
                client.getDispatcher().dispatch(new MessageEvent(tags, user, channel, msg, action));
                event = fetchMessageEvent(tags, user, channel, msg, action);
                break;
            case WHISPER:
                event = fetchPrivateMessageEvent(tags, user, msg);
                break;
            case PONG:
                event = new PongReceivedEvent();
                break;
            case PING:
                event = new PingReceivedEvent();
                break;
            case USER_NOTICE:
                // dispatching user notice event to customize own user notice event
                client.getDispatcher().dispatch(new UserNoticeEvent(tags, channel, msg));
                event = fetchSubAndRaidEvent(tags, channel, msg, client.getMessageInterface());
                break;
            case NOTICE:
                event = new NoticeEvent(tags.get("msg_id"), channel, msg);
                break;
            case HOST_TARGET:
                boolean host = StringUtils.isBlank(msg);
                String hostedChannel = message.getParameters().split(" ")[1];
                long viewers = Long.parseLong(message.getParameters().split(" ")[2].replace("[", "").replace("]", ""));
                if (host) {
                    event = new HostEvent(channel, viewers, hostedChannel);
                } else {
                    event = new UnhostEvent(channel);
                }
                break;
            case CLEAR_CHAT:
                if (msg != null) {
                    String reason = null;
                    if (tags.containsKey("ban_reason")) {
                        reason = formatValue(tags.get("ban_reason"));
                    }
                    user = client.getMessageInterface().createPrivateMessage(msg);

                    if (tags.containsKey("ban_duration")) {
                        long seconds = Long.parseLong(tags.get("ban_duration"));
                        event = new TimeoutEvent(channel, user, seconds, reason);
                    } else {
                        event = new BanEvent(channel, user, reason);
                    }
                } else event = new ClearChatEvent(channel);
                break;
            case ROOM_STATE:
                if (tags.size() == 1) {
                    String key = (String) tags.keySet().toArray()[0];
                    String value = tags.get(key);
                    event = new RoomStateChangedEvent(key, value, channel);
                } else {
                    Locale broadcastLang = StringUtils.isNotBlank(tags.get("broadcast_lang")) ? Locale.forLanguageTag(tags.get("broadcast_lang")) : null;
                    boolean r9k = tags.get("r9k").equals("1");
                    long slow = Long.parseLong(tags.get("slow"));
                    boolean sub = tags.get("subs-only").equals("1");
                    event = new RoomStateEvent(broadcastLang, r9k, slow, sub, channel);
                }
                break;
            case GLOBAL_USER_STATE:
                Color color = getColor(tags.get("color"));
                String displayName = tags.get("display_name");
                boolean turbo = tags.get("mod").equals("1");
                String userType = tags.get("user_type");
                long userId = Long.parseLong(tags.get("user_id"));
                event = new GlobalUserStateEvent(color, displayName, turbo, userId, userType);
                break;
            case USER_STATE:
                event = getUserState(tags, channel);
                break;
            default:
                break;
        }
        // do not dispatch event if it is not defined or it is nullable
        if (!Objects.isNull(event)) {
            client.getDispatcher().dispatch(event);
        }
    }

    private static UserStateEvent getUserState(Map<String, String> tags, IChannel channel) {
        Color color = getColor(tags.get("color"));
        String displayName = tags.get("display_name");
        boolean mod = tags.get("mod").equals("1");
        boolean subscriber = tags.get("mod").equals("1");
        boolean turbo = tags.get("mod").equals("1");
        String userType = tags.get("user_type");
        return new UserStateEvent(color, displayName, mod, subscriber, turbo, userType, channel);
    }

    private static Event fetchSubAndRaidEvent(Map<String, String> tags, IChannel channel, String message, IMessageInterface tmi) {
        List<Badge> badges = Arrays.stream(tags.get("badges").split(","))
                .map(badge -> new Badge(badge.split("/")[0], Integer.parseInt(badge.split("/")[1])))
                .collect(Collectors.toList());
        Color color = getColor(tags.get("color"));
        IUser user = tmi.createPrivateMessage(tags.get("login"));
        boolean mod = tags.containsKey("mod") && tags.get("mod").equals("1");
        String messageId = tags.get("msg_id");
        Long channelId = Long.parseLong(tags.get("room_id"));
        boolean subscriber = tags.containsKey("subscriber") && tags.get("subscriber").equals("1");
        boolean turbo = tags.containsKey("turbo") && tags.get("turbo").equals("1");
        String userType = tags.get("user_type");

        Event event = null;

        switch (messageId) {
            case "raid":
                long viewcount = Long.parseLong(tags.get("msg-param-viewerCount"));
                event = new RaidEvent(viewcount, badges, color, user, mod, subscriber, turbo, channel, message);
                break;
            case "ritual":
                if (tags.containsKey("msg-param-ritual-name") && tags.get("msg-param-ritual-name").equals("new_chatter")) {
                    event = new NewChatterEvent(badges, color, user, mod, subscriber, turbo, channel, message);
                } else {
                    event = new RitualNoticeEvent(badges, color, user, mod, subscriber, turbo, channel, message);
                }
                break;
            case "sub":
            case "resub":
                Subscription.Type subType;
                int months = tags.containsKey("msg-param-months") ? Integer.parseInt(tags.get("msg-param-months")) : 1;
                switch (tags.get("msg-param-sub-plan")) {
                    case "Prime":
                        subType = Subscription.Type.PRIME;
                        break;
                    case "1000":
                        subType = Subscription.Type.SUB_1000;
                        break;
                    case "2000":
                        subType = Subscription.Type.SUB_2000;
                        break;
                    case "3000":
                        subType = Subscription.Type.SUB_3000;
                        break;
                    default:
                        subType = Subscription.Type.UNKNOWN;
                }
                event = new SubscribeEvent(months, subType, badges, color, user, mod, subscriber, turbo, channel, message);
        }
        return event;
    }

    private static PrivateMessageEvent fetchPrivateMessageEvent(Map<String, String> tags, IUser user, String msg) {
        return new PrivateMessageEvent(user, msg);
    }

    private static Event fetchMessageEvent(Map<String, String> tags, IUser user, IChannel channel, String message, boolean isAction) {
        List<Badge> badges = Arrays.stream(tags.get("badges").split(","))
                .map(badge -> new Badge(badge.split("/")[0], Integer.parseInt(badge.split("/")[1])))
                .collect(Collectors.toList());
        int bits = tags.containsKey("bits") ? Integer.parseInt(tags.get("bits")) : 0;
        Color color = getColor(tags.get("color"));
        String displayName = tags.get("display_name");
        // TODO: Emotes
        boolean mod = tags.containsKey("mod") && tags.get("mod").equals("1");
        Long channelId = Long.parseLong(tags.get("room_id"));
        boolean subscriber = tags.containsKey("subscriber") && tags.get("subscriber").equals("1");
        boolean turbo = tags.containsKey("turbo") && tags.get("turbo").equals("1");
        Long userId = Long.parseLong(tags.get("user_id"));
        String userType = tags.get("user_type");
        if (bits > 0) {
            return new BitsMessageEvent(badges, bits, color, mod, subscriber, turbo, user, channel, message);
        } else if (isAction) {
            return new ActionMessageEvent(badges, color, mod, subscriber, turbo, user, channel, message);
        } else {
            return new OrdinalMessageEvent(badges, color, mod, subscriber, turbo, user, channel, message);
        }
    }

    private static Color getColor(String color) {
        if (StringUtils.isBlank(color)) return null;
        if (color.startsWith("#")) color = "0x" + color.substring(1);
        return Color.decode(color);
    }

    private static String getUserFromHostmask(String hostmask) {
        if (hostmask.contains("!") && hostmask.contains("@")) {
            return hostmask.substring(hostmask.indexOf("!") + 1, hostmask.indexOf("@"));
        } else return null;
    }

    private static String formatValue(String value) {
        return value.replace("\\s", " ");
    }

    /**
     * Overwrite toString
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!tags.isEmpty()) {
            sb.append("Tags(").append(tags).append(") ");
        }
        sb.append(":").append(hostmask).append(" ")
                .append(command.name().replace("RPL_", ""));
        if (StringUtils.isNotBlank(parameters)) {
            sb.append(" ").append(parameters);
        }
        if (StringUtils.isNotBlank(message)) {
            sb.append(" :").append(message);
        }
        return sb.toString();
    }

    /**
     * Parser States
     */
    private enum ParserState {
        STATE_NONE,
        STATE_V3,
        STATE_PREFIX,
        STATE_COMMAND,
        STATE_PARAM,
        STATE_TRAILING
    }
}
