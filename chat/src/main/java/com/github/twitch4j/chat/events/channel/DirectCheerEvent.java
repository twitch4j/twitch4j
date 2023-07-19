package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.util.DonationAmount;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * This event gets called when a user does a direct cheer in an eligible channel for this experiment.
 *
 * @see <a href="https://help.twitch.tv/s/article/cheering-experiment-2022">Twitch Information</a>
 * @deprecated in favor of {@link ChannelMessageEvent#getElevatedChatPayment()}
 */
@Value
@Unofficial
@EqualsAndHashCode(callSuper = true)
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class DirectCheerEvent extends CheerEvent {

    /**
     * Creator receives 80% of the amount after fees, during the experiment.
     *
     * @see <a href="https://help.twitch.tv/s/article/cheering-experiment-2022?language=en_US#StreamerFAQ">Help Article</a>
     */
    private static final float CREATOR_REVENUE_SPLIT = 0.8F;

    /**
     * The parsed direct cheer payment information.
     */
    DonationAmount cheer;

    /**
     * Event Constructor
     *
     * @param event The raw message event.
     * @deprecated This experiment is no longer running.
     */
    @Deprecated
    public DirectCheerEvent(IRCMessageEvent event) {
        this(
            event,
            event.getTagValue("msg-param-amount").map(Long::parseLong).orElse(0L),
            event.getTagValue("msg-param-exponent").map(Integer::parseInt).orElse(2),
            event.getTagValue("msg-param-currency").orElse("USD")
        );
    }

    DirectCheerEvent(IRCMessageEvent event, long amount, int exponent, String currency) {
        super(event,
            event.getChannel(),
            event.getUser(),
            event.getMessage().orElse(""),
            "USD".equalsIgnoreCase(currency) ? Math.round(amount * CREATOR_REVENUE_SPLIT) : 0,
            event.getSubscriberMonths().orElse(0),
            event.getSubscriptionTier().orElse(0),
            event.getFlags());

        this.cheer = new DonationAmount(amount, exponent, currency);
    }

    /**
     * @return the amount of this direct cheer, before the revenue split, and in arbitrary currency and decimal places.
     */
    public Integer getAmount() {
        return cheer.getValue().intValue();
    }

    /**
     * @return the parsed currency used in this direct cheer, or throws if unknown.
     */
    public Currency getCurrency() {
        return cheer.getParsedCurrency();
    }

    /**
     * @return the parsed monetary value that was directly cheered. For example, $50 is parsed to 50
     */
    public BigDecimal getMonetaryValue() {
        return cheer.getParsedValue();
    }

    /**
     * @return the twitch notification text for this event (which is distinct from the user's message attached to the cheer).
     */
    public String getSystemMessage() {
        return messageEvent.getTagValue("system-msg").map(String::trim).orElse("");
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated direct cheer values are not easily translatable into bit amounts, especially for non-USD currencies.
     */
    @Deprecated
    @Override
    public Integer getBits() {
        return super.getBits();
    }

}
