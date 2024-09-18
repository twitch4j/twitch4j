package com.github.twitch4j.chat.events;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.chat.enums.NoticeTag;
import com.github.twitch4j.chat.events.channel.*;
import com.github.twitch4j.chat.events.roomstate.*;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.AnnouncementColor;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.github.twitch4j.common.util.TwitchUtils.ANONYMOUS_CHEERER;
import static com.github.twitch4j.common.util.TwitchUtils.ANONYMOUS_GIFTER;

/**
 * IRC Event Handler
 * <p>
 * Listens for any irc triggered events and created the corresponding events for the EventDispatcher.
 */
@Getter
@Slf4j
@ApiStatus.Internal
public class IRCEventHandler {

    /**
     * Event Manager
     */
    private final IEventManager eventManager;

    /**
     * Constructor
     *
     * @param eventManager The event manager
     */
    @ApiStatus.Internal
    public IRCEventHandler(EventManager eventManager) {
        this.eventManager = eventManager;

        // register event handlers
        eventManager.onEvent("twitch4j-chat-event-trigger", IRCMessageEvent.class, this::handle);
    }

    private void handle(IRCMessageEvent e) {
        if (onChannelMessage(e)) return;
        if (onChannelCheer(e)) return;
        if (onChannelSubscription(e)) return;
        if (onWhisper(e)) return;
        if (onUserState(e)) return;
        if (onChannnelClientJoinEvent(e)) return;
        if (onChannnelClientLeaveEvent(e)) return;
        if (onClearChat(e)) return;
        if (onChannelState(e)) return;
        if (onClearMsg(e)) return;
        if (onAnnouncement(e)) return;
        if (onCharityDonation(e)) return;
        if (onGlobalUserState(e)) return;
        if (onNoticeEvent(e)) return;
        if (onViewerMilestone(e)) return;
        if (onRaid(e)) return;
        if (onNames(e)) return;
        if (onPayForward(e)) return;
        if (onBitsBadgeTier(e)) return;
        if (onRewardGift(e)) return;
        if (onChannelModChange(e)) return;
        if (onUnraid(e)) return;
        if (onGiftReceived(e)) return;
    }

    @Unofficial
    public boolean onAnnouncement(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equalsIgnoreCase("announcement", getNoticeType(event))) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();
            String message = event.getMessage().orElse("");
            String color = event.getTagValue("msg-param-color").orElse(AnnouncementColor.PRIMARY.toString());

            // Dispatch Event
            eventManager.publish(new ModAnnouncementEvent(event, channel, user, message, AnnouncementColor.parseColor(color)));
            return true;
        }
        return false;
    }

    /**
     * ChatChannel Message Event
     * @param event IRCMessageEvent
     */
    public boolean onChannelMessage(IRCMessageEvent event) {
        if(event.getCommandType().equals("PRIVMSG")) {
            if (event.getRawTag("bits") == null && event.getMessage().isPresent()) {
                // Load Info
                EventChannel channel = event.getChannel();
                EventUser user = event.getUser();
                String message = event.getMessage().get();

                // Dispatch Event
                if (message.startsWith("\u0001ACTION ") && message.endsWith("\u0001")) {
                    // Action
                    eventManager.publish(new ChannelMessageActionEvent(channel, event, user, message.substring(8, message.length() - 1)));
                    return true;
                } else {
                    // Regular Message
                    eventManager.publish(new ChannelMessageEvent(channel, event, user, message));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Whisper Event
     * @param event IRCMessageEvent
     */
    public boolean onWhisper(IRCMessageEvent event) {
        if(event.getCommandType().equals("WHISPER")) {
            // Load Info
            EventUser user = event.getUser();

            // Dispatch Event
            eventManager.publish(new PrivateMessageEvent(user, event.getMessage().get(), event.getClientPermissions()));
            return true;
        }
        return false;
    }

    /**
     * ChatChannel Bits Badge Earned Event Parser
     *
     * @param event the {@link IRCMessageEvent} to be checked
     */
    public boolean onBitsBadgeTier(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equalsIgnoreCase("bitsbadgetier", event.getRawTag("msg-id"))) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();

            String thresholdParam = event.getRawTagString("msg-param-threshold");
            int bitsThreshold = thresholdParam != null ? Integer.parseInt(thresholdParam) : -1;

            // Dispatch Event
            eventManager.publish(new BitsBadgeEarnedEvent(channel, user, bitsThreshold));
            return true;
        }
        return false;
    }

    /**
     * ChatChannel Cheer (Bits) Event
     * @param event IRCMessageEvent
     */
    public boolean onChannelCheer(IRCMessageEvent event) {
        if(event.getCommandType().equals("PRIVMSG")) {
            String rawBits = event.getRawTagString("bits");
            if (rawBits != null) {
                // Load Info
                EventChannel channel = event.getChannel();
                EventUser user = event.getUser();
                String message = event.getMessage().orElse("");
                Integer bits = Integer.parseInt(rawBits);
                int subMonths = event.getSubscriberMonths().orElse(0);
                int subTier = event.getSubscriptionTier().orElse(0);

                // Dispatch Event
                eventManager.publish(new CheerEvent(event, channel, user != null ? user : ANONYMOUS_CHEERER, message, bits, subMonths, subTier, event.getFlags()));
                return true;
            }
        }
        return false;
    }

    /**
     * ChatChannel Direct Cheer (Currency) Event
     *
     * @param event IRCMessageEvent
     */
    @Unofficial
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public void onDirectCheer(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equals("midnightsquid", event.getRawTag("msg-id"))) {
            eventManager.publish(new DirectCheerEvent(event));
        }
    }

    /**
     * ChatChannel Subscription Event
     *
     * @param event IRCMessageEvent
     */
    public boolean onChannelSubscription(IRCMessageEvent event) {
        final String msgId;
        if (event.getCommandType().equals("USERNOTICE") && (msgId = Objects.toString(getNoticeType(event), null)) != null) {
            EventChannel channel = event.getChannel();

            // Resub message for a multi-month gifted subscription
            if (msgId.equalsIgnoreCase("resub") && StringUtils.equalsIgnoreCase("true", event.getRawTag("msg-param-was-gifted"))) {
                eventManager.publish(new GiftedMultiMonthSubCourtesyEvent(event));
                return true;
            }
            // Sub
            else if (msgId.equalsIgnoreCase("sub") || msgId.equalsIgnoreCase("resub")) {
                // Load Info
                EventUser user = event.getUser();
                String subPlan = event.getTagValue("msg-param-sub-plan").get();
                int cumulativeMonths = event.getTagValue("msg-param-cumulative-months").map(Integer::parseInt).orElse(0);
                //according to the Twitch docs, msg-param-months is used only for giftsubs, which are handled below

                // twitch sometimes returns 0 months for new subs
                if (cumulativeMonths == 0) {
                    cumulativeMonths = 1;
                }

                // check user's sub streak
                // Twitch API specifies that 0 is returned if the user chooses not to share their streak
                Integer streak = event.getTagValue("msg-param-streak-months").map(Integer::parseInt).orElse(0);

                // unofficial: multi month tags
                Integer multiMonthDuration = Math.max(Integer.parseInt(event.getTagValue("msg-param-multimonth-duration").orElse("1")), 1);
                Integer multiMonthTenure = Integer.parseInt(event.getTagValue("msg-param-multimonth-tenure").orElse("0"));

                // Dispatch Event
                eventManager.publish(new SubscriptionEvent(event, channel, user, subPlan, event.getMessage(), cumulativeMonths, false, null, streak, null, multiMonthDuration, multiMonthTenure, event.getFlags()));
                return true;
            }
            // Receive Gifted Sub
            else if (msgId.equalsIgnoreCase("subgift") || msgId.equalsIgnoreCase("anonsubgift")) {
                // Load Info
                EventUser user = new EventUser(event.getTagValue("msg-param-recipient-id").get(), event.getTagValue("msg-param-recipient-user-name").get());
                EventUser giftedBy = event.getUser();
                String subPlan = event.getTagValue("msg-param-sub-plan").get();
                int subStreak = event.getTagValue("msg-param-months").map(Integer::parseInt).orElse(1);

                // twitch sometimes returns 0 months for new subs
                if (subStreak == 0) {
                    subStreak = 1;
                }

                // Handle multi-month gifts
                String giftMonthsParam = event.getRawTagString("msg-param-gift-months");
                int giftMonths = giftMonthsParam != null ? Integer.parseInt(giftMonthsParam) : 1;
                // unofficial: plausible, but hasn't been observed in the wild for this msg-id
                String multiTenureParam = event.getRawTagString("msg-param-multimonth-tenure");
                Integer multiMonthTenure = StringUtils.isEmpty(multiTenureParam) ? null : Integer.parseInt(multiTenureParam);

                // Dispatch Event
                eventManager.publish(new SubscriptionEvent(event, channel, user, subPlan, event.getMessage(), subStreak, true, giftedBy != null ? giftedBy : ANONYMOUS_GIFTER, 0, giftMonths, giftMonths, multiMonthTenure, event.getFlags()));
                return true;
            }
            // Gift X Subs
            else if (msgId.equalsIgnoreCase("submysterygift") || msgId.equalsIgnoreCase("anonsubmysterygift")) {
                // Load Info
                EventUser user = event.getUser();
                String subPlan = event.getTagValue("msg-param-sub-plan").get();
                Integer subsGifted = event.getTagValue("msg-param-mass-gift-count").map(Integer::parseInt).orElse(0);
                Integer subsGiftedTotal = event.getTagValue("msg-param-sender-count").map(Integer::parseInt).orElse(0);

                // Dispatch Event
                eventManager.publish(new GiftSubscriptionsEvent(event, channel, user != null ? user : ANONYMOUS_GIFTER, subPlan, subsGifted, subsGiftedTotal));
                return true;
            }
            // Upgrading from a gifted sub
            else if (msgId.equalsIgnoreCase("giftpaidupgrade") || msgId.equalsIgnoreCase("anongiftpaidupgrade")) {
                // Load Info
                EventUser user = event.getUser();
                String promoName = event.getTagValue("msg-param-promo-name").orElse(null);
                String giftTotalParam = event.getRawTagString("msg-param-promo-gift-total");
                Integer giftTotal = giftTotalParam != null ? Integer.parseInt(giftTotalParam) : null;
                String senderLogin = event.getTagValue("msg-param-sender-login").orElse(null);
                String senderName = event.getTagValue("msg-param-sender-name").orElse(null);

                // Dispatch Event
                eventManager.publish(new GiftSubUpgradeEvent(event, channel, user, promoName, giftTotal, senderLogin, senderName));
                return true;
            }
            // Upgrading from a Prime sub to a normal one
            else if (msgId.equalsIgnoreCase("primepaidupgrade")) {
                // Load Info
                EventUser user = event.getUser();
                SubscriptionPlan subPlan = event.getTagValue("msg-param-sub-plan").map(SubscriptionPlan::fromString).orElse(null);

                // Dispatch Event
                eventManager.publish(new PrimeSubUpgradeEvent(event, channel, user, subPlan));
                return true;
            }
            // Extend Subscription
            else if (msgId.equalsIgnoreCase("extendsub")) {
                // Load Info
                EventUser user = event.getUser();
                SubscriptionPlan subPlan = event.getTagValue("msg-param-sub-plan").map(SubscriptionPlan::fromString).orElse(null);

                String cumMonthsParam = event.getRawTagString("msg-param-cumulative-months");
                int cumulativeMonths = cumMonthsParam != null ? Math.max(Integer.parseInt(cumMonthsParam), 1) : 1;

                String endMonthParam = event.getRawTagString("msg-param-sub-benefit-end-month");
                Month endMonth = endMonthParam != null ? Month.of(Integer.parseInt(endMonthParam)) : null;

                // Dispatch Event
                eventManager.publish(new ExtendSubscriptionEvent(channel, user, subPlan, cumulativeMonths, endMonth));
                return true;
            }
        }
        return false;
    }

    /**
     * ChatChannel Prime Community Gift Event Parser: user receives a gift from a prime member
     *
     * @param event the {@link IRCMessageEvent} to be checked
     */
    @Unofficial
    public boolean onGiftReceived(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equalsIgnoreCase("primecommunitygiftreceived", event.getRawTag("msg-id"))) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();
            String giftName = event.getTagValue("msg-param-gift-name").orElse(null);
            String recipientName = event.getTagValue("msg-param-recipient").orElse(null);

            // Dispatch Event
            eventManager.publish(new PrimeGiftReceivedEvent(channel, user, giftName, recipientName));
            return true;
        }
        return false;
    }

    /**
     * ChatChannel Pay Forward Event Parser: user pays forward a gift they previously received
     *
     * @param event the {@link IRCMessageEvent} to be checked
     */
    public boolean onPayForward(IRCMessageEvent event) {
        CharSequence msgId;
        if ("USERNOTICE".equals(event.getCommandType()) && (msgId = getNoticeType(event)) != null
            && (StringUtils.equalsIgnoreCase(msgId, "standardpayforward") || StringUtils.equalsIgnoreCase(msgId,"communitypayforward"))) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();

            // Present for both standard & community when not anonymous
            String gifterId = event.getTagValue("msg-param-prior-gifter-id").orElse(null);
            String gifterName = gifterId != null
                ? event.getTagValue("msg-param-prior-gifter-user-name").orElseGet(() -> event.getTagValue("msg-param-prior-gifter-display-name").orElse(null))
                : null;
            EventUser gifter = gifterId != null ? new EventUser(gifterId, gifterName) : null;

            // Only present for standard
            String recipientId = msgId.charAt(0) == 's' ? event.getTagValue("msg-param-recipient-id").orElse(null) : null;
            String recipientName = recipientId != null
                ? event.getTagValue("msg-param-recipient-user-name").orElseGet(() -> event.getTagValue("msg-param-recipient-display-name").orElse(null))
                : null;
            EventUser recipient = recipientId != null ? new EventUser(recipientId, recipientName) : null;

            // Dispatch Event
            eventManager.publish(new PayForwardEvent(event, channel, user, gifter, recipient));
            return true;
        }
        return false;
    }

    @Unofficial
    public boolean onCharityDonation(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equalsIgnoreCase("charitydonation", event.getRawTag("msg-id"))) {
            eventManager.publish(new CharityDonationEvent(event));
            return true;
        }
        return false;
    }

    /**
     * ChatChannel Raid Event (receiving)
     * @param event IRCMessageEvent
     */
    public boolean onRaid(IRCMessageEvent event) {
        if (event.getCommandType().equals("USERNOTICE") && StringUtils.equalsIgnoreCase("raid", getNoticeType(event))) {
            EventChannel channel = event.getChannel();
            EventUser raider = event.getUser();
            Integer viewers;
            try {
                viewers = Integer.parseInt(event.getTagValue("msg-param-viewerCount").orElse("0"));
            } catch (NumberFormatException ex) {
                viewers = 0;
            }
            eventManager.publish(new RaidEvent(event, channel, raider, viewers));
            return true;
        }
        return false;
    }

    /**
     * ChatChannel Unraid Parser: raid cancellation
     *
     * @param event the {@link IRCMessageEvent} to be checked
     */
    public boolean onUnraid(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equalsIgnoreCase("unraid", event.getRawTag("msg-id"))) {
            eventManager.publish(new RaidCancellationEvent(event.getChannel()));
            return true;
        }
        return false;
    }

    /**
     * ChatChannel Reward Gift Event Parser: monetary event triggered emotes to be shared
     *
     * @param event the {@link IRCMessageEvent} to be checked
     */
    public boolean onRewardGift(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equalsIgnoreCase("rewardgift", event.getRawTag("msg-id"))) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();
            String domain = event.getTagValue("msg-param-domain").orElse(null);
            String triggerType = event.getTagValue("msg-param-trigger-type").orElse(null);

            String selectedCountParam = event.getRawTagString("msg-param-selected-count");
            Integer selectedCount = selectedCountParam != null ? Integer.parseInt(selectedCountParam) : null;

            String totalRewardCountParam = event.getRawTagString("msg-param-total-reward-count");
            Integer totalRewardCount = totalRewardCountParam != null ? Integer.parseInt(totalRewardCountParam) : null;

            String triggerAmountParam = event.getRawTagString("msg-param-trigger-amount");
            Integer triggerAmount = triggerAmountParam != null ? Integer.parseInt(triggerAmountParam) : null;

            // Dispatch Event
            eventManager.publish(new RewardGiftEvent(channel, user, domain, triggerType, selectedCount, totalRewardCount, triggerAmount));
            return true;
        }
        return false;
    }

    /**
     * ChatChannel Ritual Event Parser: celebration of a shared viewer milestone
     *
     * @param event the {@link IRCMessageEvent} to be checked
     * @see <a href="https://twitter.com/TwitchSupport/status/1481008324502073347">Shut down announcement</a>
     * @deprecated no longer sent by twitch.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public void onRitual(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equalsIgnoreCase("ritual", event.getRawTag("msg-id"))) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();
            String ritualName = event.getTagValue("msg-param-ritual-name").orElse(null);

            // Dispatch Event
            eventManager.publish(new RitualEvent(channel, user, ritualName));
        }
    }

    /**
     * ChatChannel clearing chat, timeouting or banning user Event
     * @param event IRCMessageEvent
     */
    public boolean onClearChat(IRCMessageEvent event) {
        if (event.getCommandType().equals("CLEARCHAT")) {
            EventChannel channel = event.getChannel();
            if (event.getRawTag("target-user-id") != null) { // ban or timeout
                if (event.getRawTag("ban-duration") != null) { // timeout
                    // Load Info
                    EventUser user = event.getTargetUser();
                    Integer duration = Integer.parseInt(event.getTagValue("ban-duration").get());
                    String banReason = event.getTagValue("ban-reason").orElse("");
                    banReason = banReason.replaceAll("\\\\s", " ");
                    UserTimeoutEvent timeoutEvent = new UserTimeoutEvent(channel, user, duration, banReason);

                    // Dispatch Event
                    eventManager.publish(timeoutEvent);
                    return true;
                } else { // ban
                    // Load Info
                    EventUser user = event.getTargetUser();
                    String banReason = event.getTagValue("ban-reason").orElse("");
                    banReason = banReason.replaceAll("\\\\s", " ");
                    UserBanEvent banEvent = new UserBanEvent(channel, user, banReason);

                    // Dispatch Event
                    eventManager.publish(banEvent);
                    return true;
                }
            } else { // Clear chat event
                eventManager.publish(new ClearChatEvent(channel));
                return true;
            }
        }
        return false;
    }

    /**
     * A single message was deleted in a channel by a moderator
     *
     * @param event IRCMessageEvent
     * @see <a href="https://dev.twitch.tv/docs/irc/tags#clearmsg-twitch-tags">Official documentation</a>
     */
    public boolean onClearMsg(IRCMessageEvent event) {
        if ("CLEARMSG".equals(event.getCommandType())) {
            EventChannel channel = event.getChannel();
            String userName = event.getUserName();
            String msgId = event.getTagValue("target-msg-id").orElse(null);
            String message = event.getMessage().orElse("");
            boolean wasActionMessage = message.startsWith("\u0001ACTION ") && message.endsWith("\u0001");
            String trimmedMsg = wasActionMessage ? message.substring("\u0001ACTION ".length(), message.length() - "\u0001".length()) : message;
            eventManager.publish(new DeleteMessageEvent(event, channel, userName, msgId, trimmedMsg, wasActionMessage));
            return true;
        }
        return false;
    }

    /**
     * Names list is received upon initial channel join
     *
     * @param event {@link IRCMessageEvent}
     */
    public boolean onNames(IRCMessageEvent event) {
        if ("353".equals(event.getCommandType()) && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
            EventChannel channel = event.getChannel();
            event.getMessage()
                .map(String::trim)
                .map(names -> StringUtils.split(names, ' '))
                .ifPresent(names ->
                    Arrays.stream(names)
                        .map(String::trim)
                        .filter(StringUtils::isNotEmpty)
                        .map(login -> new EventUser(null, login))
                        .forEach(user -> eventManager.publish(new ChannelJoinEvent(channel, user)))
                );
            return true;
        }
        return false;
    }

    /**
     * User Joins ChatChannel Event
     * @param event IRCMessageEvent
     */
    public boolean onChannnelClientJoinEvent(IRCMessageEvent event) {
        if(event.getCommandType().equals("JOIN") && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();

            // Dispatch Event
            if (channel != null && user != null) {
                eventManager.publish(new ChannelJoinEvent(channel, user));
                return true;
            }
        }
        return false;
    }

    /**
     * User Leaves ChatChannel Event
     * @param event IRCMessageEvent
     */
    public boolean onChannnelClientLeaveEvent(IRCMessageEvent event) {
        if(event.getCommandType().equals("PART") && event.getChannelName().isPresent() && event.getClientName().isPresent()) {
            // Load Info
            EventChannel channel = event.getChannel();
            EventUser user = event.getUser();

            // Dispatch Event
            if (channel != null && user != null) {
                eventManager.publish(new ChannelLeaveEvent(channel, user));
                return true;
            }
        }
        return false;
    }

    /**
     * Mod Status Change Event
     * @param event IRCMessageEvent
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public boolean onChannelModChange(IRCMessageEvent event) {
        if(event.getCommandType().equals("MODE") && event.getPayload().isPresent()) {
            // Receiving Mod Status
            if(event.getPayload().get().substring(1).startsWith("o")) {
                // Load Info
                EventChannel channel = event.getChannel();
                EventUser user = new EventUser(null, event.getPayload().get().substring(3));

                // Dispatch Event
                eventManager.publish(new ChannelModEvent(channel, user, event.getPayload().get().startsWith("+")));
                return true;
            }
        }
        return false;
    }

    public boolean onNoticeEvent(IRCMessageEvent event) {
        if (event.getCommandType().equals("NOTICE")) {
            EventChannel channel = event.getChannel();
            String messageId = event.getTagValue("msg-id").get();
            String message = event.getMessage().orElse(null); // can be null, ie. bad_delete_message_error

            eventManager.publish(new ChannelNoticeEvent(channel, messageId, message));
            return true;
        }
        return false;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public void onHostOnEvent(IRCMessageEvent event) {
        if (event.getCommandType().equals("NOTICE")) {
            EventChannel channel = event.getChannel();
            String messageId = event.getTagValue("msg-id").get();

            if (messageId.equals(NoticeTag.HOST_ON.toString())) {
                String message = event.getMessage().get();
                String targetChannelName = message.substring(12, message.length() - 1);
                EventChannel targetChannel = new EventChannel(null, targetChannelName);
                eventManager.publish(new HostOnEvent(channel, targetChannel));
            }
        }
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public void onHostOffEvent(IRCMessageEvent event) {
        if (event.getCommandType().equals("NOTICE")) {
            EventChannel channel = event.getChannel();
            String messageId = event.getTagValue("msg-id").get();

            if (messageId.equals(NoticeTag.HOST_OFF.toString())) {
                eventManager.publish(new HostOffEvent(channel));
            }
        }
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public void onInboundHostEvent(IRCMessageEvent event) {
        if ("PRIVMSG".equals(event.getCommandType()) && "jtv".equals(event.getClientName().orElse(null)) && event.getChannelName().isPresent() && event.getRawTags().isEmpty()) {
            final String hostMessage = " is now hosting you";
            event.getMessage()
                .map(msg -> msg.indexOf(hostMessage))
                .filter(index -> index > 0)
                .map(index -> event.getMessage().get().substring(0, index))
                .map(String::trim)
                .ifPresent(hostName -> eventManager.publish(new InboundHostEvent(event.getChannelName().get(), hostName)));
        }
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public void onListModsEvent(IRCMessageEvent event) {
        if ("NOTICE".equals(event.getCommandType()) && event.getTagValue("msg-id").filter(s -> s.equals(NoticeTag.ROOM_MODS.toString()) || s.equals(NoticeTag.NO_MODS.toString())).isPresent()) {
            List<String> names = extractItemsFromDelimitedList(event.getMessage(), "The moderators of this channel are: ", ", ");
            eventManager.publish(new ListModsEvent(event.getChannel(), names));
        }
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public void onListVipsEvent(IRCMessageEvent event) {
        if ("NOTICE".equals(event.getCommandType()) && event.getTagValue("msg-id").filter(s -> s.equals(NoticeTag.VIPS_SUCCESS.toString()) || s.equals(NoticeTag.NO_VIPS.toString())).isPresent()) {
            List<String> names = extractItemsFromDelimitedList(event.getMessage(), "The VIPs of this channel are: ", ", ");
            eventManager.publish(new ListVipsEvent(event.getChannel(), names));
        }
    }

    public boolean onChannelState(IRCMessageEvent event) {
        if (event.getCommandType().equals("ROOMSTATE")) {
            // getting Status on channel
            EventChannel channel = event.getChannel();
            Map<ChannelStateEvent.ChannelState, Object> states = new HashMap<>();
            event.getEscapedTags().forEach((k, v) -> {
                switch (k) {
                    case "broadcaster-lang":
                        Locale locale = v != null ? Locale.forLanguageTag(v.toString()) : null;
                        states.put(ChannelStateEvent.ChannelState.BROADCAST_LANG, locale);
                        eventManager.publish(new BroadcasterLanguageEvent(channel, locale));
                        break;
                    case "emote-only":
                        boolean eoActive = StringUtils.equals("1", v);
                        states.put(ChannelStateEvent.ChannelState.EMOTE, eoActive);
                        eventManager.publish(new EmoteOnlyEvent(channel, eoActive));
                        break;
                    case "followers-only":
                        long followDelay = Long.parseLong(v.toString());
                        states.put(ChannelStateEvent.ChannelState.FOLLOWERS, followDelay);
                        eventManager.publish(new FollowersOnlyEvent(channel, followDelay));
                        break;
                    case "r9k":
                        boolean uniqActive = StringUtils.equals("1", v);
                        states.put(ChannelStateEvent.ChannelState.R9K, uniqActive);
                        eventManager.publish(new Robot9000Event(channel, uniqActive));
                        break;
                    case "rituals":
                        boolean ritualsActive = StringUtils.equals("1", v);
                        states.put(ChannelStateEvent.ChannelState.RITUALS, ritualsActive);
                        break;
                    case "slow":
                        long slowDelay = Long.parseLong(v.toString());
                        states.put(ChannelStateEvent.ChannelState.SLOW, slowDelay);
                        eventManager.publish(new SlowModeEvent(channel, slowDelay));
                        break;
                    case "subs-only":
                        boolean subActive = StringUtils.equals("1", v);
                        states.put(ChannelStateEvent.ChannelState.SUBSCRIBERS, subActive);
                        eventManager.publish(new SubscribersOnlyEvent(channel, subActive));
                        break;
                    default:
                        break;
                }
            });
            eventManager.publish(new ChannelStateEvent(channel, states));
            return true;
        }
        return false;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.17.0")
    public void onMessageDeleteResponse(IRCMessageEvent event) {
        if (event.getCommandType().equals("NOTICE")) {
            EventChannel channel = event.getChannel();
            String messageId = event.getTagValue("msg-id").orElse(null);
            NoticeTag tag = NoticeTag.parse(messageId);

            if (tag == NoticeTag.DELETE_MESSAGE_SUCCESS) {
                eventManager.publish(new MessageDeleteSuccess(channel));
            } else if (tag == NoticeTag.BAD_DELETE_MESSAGE_ERROR || tag == NoticeTag.BAD_DELETE_MESSAGE_BROADCASTER || tag == NoticeTag.BAD_DELETE_MESSAGE_MOD) {
                eventManager.publish(new MessageDeleteError(channel));
                log.warn("Failed to delete a message in {}!", channel.getName());
            }
        }
    }

    public boolean onUserState(IRCMessageEvent event) {
        if (event.getCommandType().equals("USERSTATE")) {
            eventManager.publish(new UserStateEvent(event));
            return true;
        }
        return false;
    }

    public boolean onGlobalUserState(IRCMessageEvent event) {
        if ("GLOBALUSERSTATE".equals(event.getCommandType())) {
            eventManager.publish(new GlobalUserStateEvent(event));
            return true;
        }
        return false;
    }

    @Unofficial
    private boolean onViewerMilestone(IRCMessageEvent event) {
        if ("USERNOTICE".equals(event.getCommandType()) && StringUtils.equals(ViewerMilestoneEvent.USERNOTICE_ID, event.getRawTag("msg-id"))) {
            eventManager.publish(new ViewerMilestoneEvent(event));
            return true;
        }
        return false;
    }

    @Nullable
    private static CharSequence getNoticeType(IRCMessageEvent userNoticeEvent) {
        CharSequence value = userNoticeEvent.getRawTag("msg-id");
        if (StringUtils.equals("sharedchatnotice", value)) {
            return userNoticeEvent.getRawTag("source-msg-id");
        }
        return value;
    }

    @NonNull
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static List<String> extractItemsFromDelimitedList(@NonNull Optional<String> message, @NonNull String prefix, @NonNull String delim) {
        return message.filter(s -> s.startsWith(prefix))
            .map(s -> s.substring(prefix.length()))
            .map(s -> s.charAt(s.length() - 1) == '.' ? s.substring(0, s.length() - 1) : s) // remove trailing period if present
            .map(s -> StringUtils.split(s, delim))
            .map(Arrays::asList)
            .map(Collections::unmodifiableList)
            .orElse(Collections.emptyList());
    }

}
