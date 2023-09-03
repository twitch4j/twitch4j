package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ScheduleUpdate {
    private String scheduleUpdateType;
    private List<ScheduledAd> adSchedule;

    public boolean isSnooze() {
        return "Snooze".equalsIgnoreCase(scheduleUpdateType);
    }

    public boolean isRemoval() {
        return "RemoveAdTime".equalsIgnoreCase(scheduleUpdateType);
    }
}
