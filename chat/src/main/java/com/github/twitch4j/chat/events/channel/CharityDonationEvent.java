package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.util.DonationAmount;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Value
@Unofficial
@EqualsAndHashCode(callSuper = true)
public class CharityDonationEvent extends AbstractChannelEvent {

    @NotNull
    @EqualsAndHashCode.Exclude
    IRCMessageEvent messageEvent;

    String userId;

    String userLogin;

    String userName;

    Set<CommandPermission> badges;

    String charityName;

    DonationAmount amount;

    String systemMessage;

    public CharityDonationEvent(@NotNull IRCMessageEvent rawEvent) {
        super(rawEvent.getChannel());
        this.messageEvent = rawEvent;
        this.userId = rawEvent.getTagValue("user-id").orElse(null);
        this.userLogin = rawEvent.getTagValue("login").orElse(null);
        this.userName = rawEvent.getTagValue("display-name").orElse(userLogin);
        this.badges = rawEvent.getClientPermissions();
        this.charityName = rawEvent.getTagValue("msg-param-charity-name").orElse(null);

        Long amount = Long.parseLong(rawEvent.getTagValue("msg-param-donation-amount").orElse("0"));
        String currency = rawEvent.getTagValue("msg-param-donation-currency").orElse("USD");
        Integer decimals = Integer.parseInt(rawEvent.getTagValue("msg-param-exponent").orElse("2"));
        this.amount = new DonationAmount(amount, decimals, currency);

        this.systemMessage = rawEvent.getTagValue("system-msg").orElseGet(
            () -> String.format("%s donated %s %s to support %s", userName, currency, this.amount.getParsedValue().toPlainString(), charityName)
        );
    }

    public EventUser getUser() {
        return new EventUser(userId, userLogin);
    }

}
