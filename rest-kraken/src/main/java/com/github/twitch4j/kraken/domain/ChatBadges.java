package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @deprecated Due endpoints is deprecated decommission have been planned on <b>Febuary 28, 2022</b>.
 *             More details about decommission finds <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>
 */
@Data
@Deprecated
@Setter(AccessLevel.PRIVATE)
public class ChatBadges {
    private ChatBadge admin;
    private ChatBadge broadcaster;
    private ChatBadge globalMod;
    private ChatBadge mod;
    private ChatBadge staff;
    private ChatBadge subscriber;
    private ChatBadge turbo;
}
