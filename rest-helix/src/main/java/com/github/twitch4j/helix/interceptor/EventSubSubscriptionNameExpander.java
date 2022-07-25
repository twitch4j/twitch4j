package com.github.twitch4j.helix.interceptor;

import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import feign.Param;

public class EventSubSubscriptionNameExpander implements Param.Expander {
    @Override
    public String expand(Object value) {
        if (value instanceof SubscriptionType<?, ?, ?>)
            return ((SubscriptionType<?, ?, ?>) value).getName();
        return null;
    }
}
