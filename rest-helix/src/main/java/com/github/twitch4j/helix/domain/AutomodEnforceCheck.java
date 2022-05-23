package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Value
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutomodEnforceCheck {
    /**
     * Developer-generated identifier for mapping messages to results.
     */
    @NotNull
    String msgId;

    /**
     * Message text.
     */
    @NotNull
    String msgText;

    /**
     * User ID of the sender.
     *
     * @see <a href="https://discuss.dev.twitch.tv/t/upcoming-changes-to-the-check-automod-status-api-endpoint/38512#deprecation-of-user_id-in-the-request-3">Deprecation announcement</a>
     * @deprecated The AutoMod service will no longer take into account the status of a user to determine whether a string message meets the channel’s AutoMod requirements on June 20, 2022
     */
    @Deprecated
    @Nullable
    String userId;

    /**
     * Constructs a message object to be checked against AutoMod enforcement settings
     *
     * @param message  The message to be checked
     * @param senderId The channel in which the message should be checked
     * @see com.github.twitch4j.helix.TwitchHelix#checkAutomodStatus(String, String, AutomodEnforceCheckList)
     * @deprecated senderId is deprecated by <a href="https://discuss.dev.twitch.tv/t/upcoming-changes-to-the-check-automod-status-api-endpoint/38512#deprecation-of-user_id-in-the-request-3">Twitch</a>
     */
    @Deprecated
    public AutomodEnforceCheck(@NotNull String message, @Nullable String senderId) {
        this.msgId = UUID.randomUUID().toString();
        this.msgText = message;
        this.userId = senderId;
    }

    /**
     * Constructs a message object to be checked against AutoMod enforcement settings
     *
     * @param message The message to be checked
     * @see com.github.twitch4j.helix.TwitchHelix#checkAutomodStatus(String, String, AutomodEnforceCheckList)
     */
    public AutomodEnforceCheck(@NotNull String message) {
        this.msgId = UUID.randomUUID().toString();
        this.msgText = message;
        this.userId = null;
    }

}
