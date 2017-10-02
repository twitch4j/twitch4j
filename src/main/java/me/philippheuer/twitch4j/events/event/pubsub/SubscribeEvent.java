package me.philippheuer.twitch4j.events.event.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.philippheuer.twitch4j.enums.SubPlan;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SubscribeEvent extends PubSubChatEvent {
    private SubPlan subPlan;
    private String subPlanName;
    private int months;
    @JsonProperty("sub_message") private Message message;
}
