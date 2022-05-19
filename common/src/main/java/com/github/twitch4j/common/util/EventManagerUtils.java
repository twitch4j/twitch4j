package com.github.twitch4j.common.util;

import com.github.philippheuer.events4j.core.EventManager;

public class EventManagerUtils {

    /**
     * Validates the provided EventManager or initializes a new one
     *
     * @param eventManager        EventManager
     * @param defaultEventHandler The default eventHandler class
     * @return EventManager
     */
    public static EventManager validateOrInitializeEventManager(EventManager eventManager, Class<?> defaultEventHandler) {
        EventManager em = eventManager != null ? eventManager : initializeEventManager(defaultEventHandler);
        validateEventManager(em);
        return em;
    }

    /**
     * Initializes a new EventManager instance.
     *
     * @param defaultEventHandler The default eventHandler class
     * @return EventManager
     */
    public static EventManager initializeEventManager(Class<?> defaultEventHandler) {
        EventManager eventManager = new EventManager();

        eventManager.autoDiscovery();
        eventManager.setDefaultEventHandler(defaultEventHandler);

        return eventManager;
    }

    /**
     * Checks if the provided EventManager is usable by Twitch4J
     *
     * @param eventManager The eventManager instance
     */
    public static void validateEventManager(EventManager eventManager) {
        if (eventManager.getEventHandlers().size() == 0) {
            throw new RuntimeException("Fatal: No EventHandlers have been registered in the EventManager, please run the autodiscovery to add EventHandlers that are present in your classpath. -> eventHandler.autoDiscovery();");
        }
        if (eventManager.getDefaultEventHandler() == null) {
            throw new RuntimeException("Fatal: Twitch4J will not be functional unless you set a defaultEventHandler that can be used for internal events (eventManager.setDefaultEventHandler)!");
        }
    }

}
