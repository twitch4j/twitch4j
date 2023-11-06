package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum NoticeType {
    SUB,
    RESUB,
    SUB_GIFT,
    COMMUNITY_SUB_GIFT,
    GIFT_PAID_UPGRADE,
    PRIME_PAID_UPGRADE,
    RAID,
    UNRAID,
    PAY_IT_FORWARD,
    ANNOUNCEMENT,
    BITS_BADGE_TIER,
    CHARITY_DONATION,
    @JsonEnumDefaultValue
    UNKNOWN
}
