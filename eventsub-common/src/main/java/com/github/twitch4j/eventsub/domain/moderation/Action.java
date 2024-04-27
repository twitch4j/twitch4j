package com.github.twitch4j.eventsub.domain.moderation;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Action {
    BAN,
    TIMEOUT,
    UNBAN,
    UNTIMEOUT,
    CLEAR,
    EMOTEONLY,
    EMOTEONLYOFF,
    FOLLOWERS,
    FOLLOWERSOFF,
    UNIQUECHAT,
    UNIQUECHATOFF,
    SLOW,
    SLOWOFF,
    SUBSCRIBERS,
    SUBSCRIBERSOFF,
    UNRAID,
    DELETE,
    UNVIP,
    VIP,
    RAID,
    ADD_BLOCKED_TERM,
    ADD_PERMITTED_TERM,
    @JsonAlias("remove_blocked_term") // this is what's actually sent, unlike what the docs claim
    DELETE_BLOCKED_TERM,
    @JsonAlias("remove_permitted_term") // this is what's actually sent, unlike what the docs claim
    DELETE_PERMITTED_TERM,
    MOD,
    UNMOD,
    APPROVE_UNBAN_REQUEST,
    DENY_UNBAN_REQUEST,
    @JsonEnumDefaultValue
    UNKNOWN
}
