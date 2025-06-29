package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.HypeTrainType;
import com.github.twitch4j.eventsub.domain.SharedChatParticipant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class HypeTrainV2Event extends HypeTrainEvent {

    /**
     * Optional. Non-null for a shared Hype Train. Contains the list of broadcasters in the shared Hype Train.
     */
    @Nullable
    private List<SharedChatParticipant> sharedTrainParticipants;

    /**
     * Indicates if the Hype Train is shared.
     * When true, {@link #getSharedTrainParticipants()} will contain the list of broadcasters the train is shared with.
     */
    @JsonProperty("is_shared_train")
    private boolean isSharedTrain;

    /**
     * The type of the Hype Train.
     */
    private HypeTrainType type;

    @Override
    public boolean isGoldenKappaTrain() {
        return this.type == HypeTrainType.GOLDEN_KAPPA;
    }

}
