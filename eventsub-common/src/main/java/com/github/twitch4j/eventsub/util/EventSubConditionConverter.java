package com.github.twitch4j.eventsub.util;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.condition.EventSubCondition;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionType;
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class EventSubConditionConverter {

    public <T extends EventSubCondition> T getCondition(Class<T> conditionClass, Map<String, Object> condition) {
        return conditionClass != null ? TypeConvert.convertValue(condition, conditionClass) : null;
    }

    public EventSubCondition getCondition(String type, String version, Map<String, Object> condition) {
        return getCondition(SubscriptionTypes.getSubscriptionType(type, version), condition);
    }

    public EventSubCondition getCondition(SubscriptionType<?, ?, ?> type, Map<String, Object> condition) {
        return getCondition(type != null ? type.getConditionClass() : null, condition);
    }

}
