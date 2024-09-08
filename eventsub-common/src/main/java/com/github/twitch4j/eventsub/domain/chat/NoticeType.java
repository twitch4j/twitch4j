package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import org.jetbrains.annotations.ApiStatus;

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
    @ApiStatus.Experimental
    SHARED_CHAT_SUB,
    @ApiStatus.Experimental
    SHARED_CHAT_RESUB,
    @ApiStatus.Experimental
    SHARED_CHAT_SUB_GIFT,
    @ApiStatus.Experimental
    SHARED_CHAT_COMMUNITY_SUB_GIFT,
    @ApiStatus.Experimental
    SHARED_CHAT_GIFT_PAID_UPGRADE,
    @ApiStatus.Experimental
    SHARED_CHAT_PRIME_PAID_UPGRADE,
    @ApiStatus.Experimental
    SHARED_CHAT_RAID,
    @ApiStatus.Experimental
    SHARED_CHAT_PAY_IT_FORWARD,
    @ApiStatus.Experimental
    SHARED_CHAT_ANNOUNCEMENT,
    @JsonEnumDefaultValue
    UNKNOWN
}
