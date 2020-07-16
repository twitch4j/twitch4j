package com.github.twitch4j.common.events;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import lombok.AllArgsConstructor;

/**
 * Forwards all events to a different {@link IEventManager}.
 * <p>
 * Be wary of infinite loops from misuse.
 */
@AllArgsConstructor
public class ForwardingEventHandler implements IEventHandler {

    private IEventManager eventManager;

    @Override
    public void publish(Object event) {
        if (eventManager != null)
            eventManager.publish(event);
    }

    @Override
    public void close() {
        this.eventManager = null;
    }

}
