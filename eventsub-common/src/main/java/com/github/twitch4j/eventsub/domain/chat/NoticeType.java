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
    SHARED_CHAT_SUB,
    SHARED_CHAT_RESUB,
    SHARED_CHAT_SUB_GIFT,
    SHARED_CHAT_COMMUNITY_SUB_GIFT,
    SHARED_CHAT_GIFT_PAID_UPGRADE,
    SHARED_CHAT_PRIME_PAID_UPGRADE,
    SHARED_CHAT_RAID,
    SHARED_CHAT_PAY_IT_FORWARD,
    SHARED_CHAT_ANNOUNCEMENT,
    @JsonEnumDefaultValue
    UNKNOWN
}
