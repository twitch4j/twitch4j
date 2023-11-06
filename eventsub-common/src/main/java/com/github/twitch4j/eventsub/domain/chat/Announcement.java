package com.github.twitch4j.eventsub.domain.chat;

import com.github.twitch4j.common.enums.AnnouncementColor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Announcement {

    /**
     * Color of the announcement.
     */
    private AnnouncementColor color;

}
