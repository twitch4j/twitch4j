package com.github.twitch4j.common.builder;

import com.github.philippheuer.events4j.core.EventManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TwitchEventAwareAPIBuilder<T> extends TwitchAPIBuilder<T> {

    /**
     * Event Manager
     */
    private EventManager eventManager = new EventManager();

    /**
     * With EventManager
     *
     * @param eventManager EventManager
     * @return T
     */
    public T withEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        return (T) this;
    }

}
