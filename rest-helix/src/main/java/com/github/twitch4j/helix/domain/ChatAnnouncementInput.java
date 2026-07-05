package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.twitch4j.common.enums.AnnouncementColor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatAnnouncementInput {

    /**
     * The announcement to make in the broadcaster’s chat room.
     * <p>
     * Announcements are limited to a maximum of 500 characters;
     * announcements longer than 500 characters are truncated.
     */
    @NotNull
    private String message;

    /**
     * The color used to highlight the announcement.
     * <p>
     * If color is set to primary or is not set, the channel’s accent color is used to highlight the announcement.
     *
     * @see <a href="https://www.twitch.tv/settings/profile">Profile Accent Color under profile settings, Channel and Videos, and Brand</a>
     */
    @Nullable
    @Builder.Default
    private AnnouncementColor color = AnnouncementColor.PRIMARY;

    /**
     * Determines if the chat announcement is sent only to the source channel
     * (defined by broadcaster_id) during a shared chat session.
     * <p>
     * This has no effect if the announcement is not sent during a shared chat session.
     * <p>
     * The default value when using an App Access Token is true.
     * If you prefer to send an announcement to all channels in a shared chat session, set this parameter to false.
     * <p>
     * NOTE: This parameter can only be set when utilizing an App Access Token.
     * It cannot be specified when a User Access Token is used, and will instead result in an HTTP 400 error.
     */
    @Nullable
    private Boolean forSourceOnly;

}
