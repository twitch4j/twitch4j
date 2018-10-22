package twitch4j.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Channel {

    /**
     * Channel Id
     */
    private final Long id;

    /**
     * Channel Name
     */
    private final String name;

}
