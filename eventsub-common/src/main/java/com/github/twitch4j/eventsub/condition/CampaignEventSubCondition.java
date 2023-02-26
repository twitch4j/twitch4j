package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.defaultString;

@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@Jacksonized
public class CampaignEventSubCondition extends EventSubCondition {

    /**
     * Required: The organization ID of the organization that owns the game on the developer portal.
     */
    private String organizationId;

    /**
     * Optional: The category (or game) ID of the game for which entitlement notifications will be received.
     */
    private String categoryId;

    /**
     * Optional: The campaign ID for a specific campaign for which entitlement notifications will be received.
     */
    private String campaignId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CampaignEventSubCondition)) return false;

        CampaignEventSubCondition that = (CampaignEventSubCondition) o;
        return defaultString(organizationId).equals(defaultString(that.organizationId))
            && defaultString(categoryId).equals(defaultString(that.categoryId))
            && defaultString(campaignId).equals(defaultString(that.campaignId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultString(organizationId), defaultString(categoryId), defaultString(campaignId));
    }
}
