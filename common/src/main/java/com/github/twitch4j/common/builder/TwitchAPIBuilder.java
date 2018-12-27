package com.github.twitch4j.common.builder;

import com.github.philippheuer.events4j.EventManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwitchAPIBuilder<T> {

    /**
     * Event Manager
     */
    @Wither
    private EventManager eventManager = new EventManager();

    /**
     * Client Id
     */
    @Wither
    private String clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";

    /**
     * Client Secret
     */
    @Wither
    private String clientSecret = "**SECRET**";

    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * Builder Method (Placeholder)
     *
     * @return
     */
    public T build() {
        throw new NotImplementedException("Builder didn't implement build yet, please check your code!");
    }

}
