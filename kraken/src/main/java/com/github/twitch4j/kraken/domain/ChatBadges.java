package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
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
