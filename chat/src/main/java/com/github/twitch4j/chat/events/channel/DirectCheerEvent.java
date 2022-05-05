package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This event gets called when a user does a direct cheer in an eligible channel for this experiment.
 *
 * @see <a href="https://help.twitch.tv/s/article/cheering-experiment-2022">Twitch Information</a>
 */
@Value
@Unofficial
@EqualsAndHashCode(callSuper = true)
public class DirectCheerEvent extends CheerEvent {

    private static final Map<String, Currency> CURRENCY_MAP;

    /**
     * Creator receives 80% of the amount after fees, during the experiment.
     *
     * @see <a href="https://help.twitch.tv/s/article/cheering-experiment-2022?language=en_US#StreamerFAQ">Help Article</a>
     */
    private static final float CREATOR_REVENUE_SPLIT = 0.8F;

    /**
     * The amount of the direct cheer.
     * <p>
     * For example, $1 = 100
     * <p>
     * Note: this is before adjustment for twitch's revenue split.
     * Use {@link #getBits()} for the adjusted value.
     */
    Integer amount;

    /**
     * The twitch notification text for this event.
     * <p>
     * For example: DisplayName Cheered with $50
     * <p>
     * Note: this is distinct from the user's message attached to the cheer ({@link #getMessage()})
     */
    @NotNull
    String systemMessage;

    /**
     * Parses the currency used in this direct cheer, or null if unknown.
     * Currently, this always resolves to USD due to the experiment restrictions.
     */
    @Nullable
    @Getter(lazy = true)
    Currency currency = parseCurrency(getSystemMessage());

    /**
     * Parses the monetary value that was directly cheered.
     * <p>
     * For example, $50 is parsed to 50
     */
    @Nullable
    @Getter(lazy = true)
    BigDecimal monetaryValue = parseValue(getSystemMessage());

    /**
     * Event Constructor
     *
     * @param event The raw message event.
     */
    public DirectCheerEvent(IRCMessageEvent event) {
        this(
            event,
            event.getTagValue("msg-param-amount").map(Integer::parseInt).orElse(null),
            event.getTagValue("system-msg").map(String::trim).orElse("")
        );
    }

    DirectCheerEvent(IRCMessageEvent event, Integer amount, @NotNull String systemMessage) {
        super(event, event.getChannel(), event.getUser(), event.getMessage().orElse(""), Math.round(amount * CREATOR_REVENUE_SPLIT), event.getSubscriberMonths().orElse(0), event.getSubscriptionTier().orElse(0), event.getFlags());
        this.amount = amount;
        this.systemMessage = systemMessage;
    }

    private static Currency parseCurrency(String systemMsg) {
        int start = systemMsg.lastIndexOf(' ');
        if (start < 0) return null; // empty message
        if (Character.isDigit(systemMsg.charAt(start + 1))) {
            start = systemMsg.substring(0, start).lastIndexOf(' '); // workaround for currencies that have a space between symbol and monetary value
        }
        start = start + 1; // skip the space character

        int end = start + 1; // default symbol has length 1
        for (; end < systemMsg.length(); end++) {
            if (Character.isDigit(systemMsg.charAt(end)))
                break; // symbol ends before digits start
        }

        return CURRENCY_MAP.get(systemMsg.substring(start, end).trim());
    }

    private static BigDecimal parseValue(String systemMsg) {
        int start = systemMsg.lastIndexOf(' ');
        if (start < 0) return null; // empty message

        for (; start < systemMsg.length(); start++) {
            if (Character.isDigit(systemMsg.charAt(start)))
                break; // value starts at first digit
        }

        int end = systemMsg.indexOf(' ', start); // value ends at next space
        if (end < 0) {
            end = systemMsg.length(); // or end of string if no spaces remaining
        }

        return new BigDecimal(systemMsg.substring(start, end));
    }

    static {
        Set<Currency> currencies = Currency.getAvailableCurrencies();

        final Map<String, Currency> map = new HashMap<>(currencies.size() * 3);
        currencies.forEach(c -> map.put(c.getSymbol(), c));
        currencies.forEach(c -> map.putIfAbsent(c.getCurrencyCode(), c)); // future proofing
        currencies.forEach(c -> map.putIfAbsent(c.getDisplayName(), c)); // future proofing

        CURRENCY_MAP = Collections.unmodifiableMap(map);
    }

}
