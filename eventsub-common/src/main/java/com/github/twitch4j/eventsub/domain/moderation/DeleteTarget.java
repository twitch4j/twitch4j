package com.github.twitch4j.eventsub.domain.moderation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeleteTarget extends UserTarget {

    /**
     * The ID of the message being deleted.
     */
    private String messageId;

    /**
     * The message body of the message being deleted.
     */
    private String messageBody;

}
