package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendPubSubMessageInput {

    /**
     * Target a specific public channel.
     */
    public static final String BROADCAST_TARGET = "broadcast";

    /**
     * Target all channels that have the extension active.
     */
    public static final String GLOBAL_TARGET = "global";

    /**
     * Target a user via private message.
     */
    public static final String WHISPER_TARGET_PREFIX = "whisper-";

    /**
     * Strings for valid PubSub targets.
     * Valid values: "broadcast", "global", "whisper-<user-id>"
     */
    @Singular
    @JsonProperty("target")
    private List<String> targets;

    /**
     * ID of the broadcaster receiving the payload.
     * This is not required if is_global_broadcast is set to true.
     */
    @Nullable
    private String broadcasterId;

    /**
     * Indicates if the message should be sent to all channels where your Extension is active.
     * <p>
     * Default: false.
     */
    @Builder.Default
    @JsonProperty("is_global_broadcast")
    private boolean globalBroadcast = false;

    /**
     * String-encoded JSON message to be sent.
     * <p>
     * Must not exceed 5 KB in size.
     */
    private String message;

    /**
     * @return whether the constructed input complies with twitch requirements.
     */
    public boolean validate() {
        // Message must be present
        if (message == null)
            return false;

        // Must be either global or targeting a broadcaster
        if (globalBroadcast == (broadcasterId != null && !broadcasterId.isEmpty()))
            return false;

        // Must have targets
        if (targets == null || targets.isEmpty())
            return false;

        // Targets must be valid
        boolean hasGlobal = false;
        for (String target : targets) {
            if (target == null || target.isEmpty())
                return false;

            if (BROADCAST_TARGET.equals(target) || target.startsWith(WHISPER_TARGET_PREFIX))
                continue;

            if (GLOBAL_TARGET.equals(target)) {
                hasGlobal = true;
                continue;
            }

            return false;
        }

        // Global target must have boolean flag enabled
        if (globalBroadcast == !hasGlobal)
            return false;

        return true;
    }

}
