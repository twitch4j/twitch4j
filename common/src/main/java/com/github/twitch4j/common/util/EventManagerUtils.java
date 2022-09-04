package com.github.twitch4j.common.util;

import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class EventManagerUtils {

    /**
     * Validates the provided EventManager or initializes a new one
     *
     * @param eventManager        EventManager
     * @param defaultEventHandler The default eventHandler class
     * @return EventManager
     */
    public static EventManager validateOrInitializeEventManager(@Nullable EventManager eventManager, @NotNull Class<?> defaultEventHandler) {
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
    public static EventManager initializeEventManager(@NotNull Class<?> defaultEventHandler) {
        EventManager eventManager = new EventManager();

        // Try to register just defaultEventHandler
        try {
            eventManager.registerEventHandler((IEventHandler) defaultEventHandler.getDeclaredConstructor().newInstance());
            eventManager.setDefaultEventHandler(defaultEventHandler);
        } catch (Exception e) {
            log.warn("Failed to register the requested default event handler: " + defaultEventHandler, e);
        }

        // Fallback
        if (eventManager.getDefaultEventHandler() == null) {
            eventManager.autoDiscovery();
            if (!eventManager.getEventHandlers().isEmpty()) {
                Class<? extends IEventHandler> clazz = eventManager.getEventHandlers().get(0).getClass();
                eventManager.setDefaultEventHandler(clazz);
                log.info("Registered fallback default event handler: {}", clazz.getTypeName());
            }
        }

        return eventManager;
    }

    /**
     * Checks if the provided EventManager is usable by Twitch4J
     *
     * @param eventManager The eventManager instance
     */
    public static void validateEventManager(@NotNull EventManager eventManager) {
        if (eventManager.getEventHandlers().size() == 0) {
            throw new RuntimeException("Fatal: No EventHandlers have been registered in the EventManager, please run the autodiscovery to add EventHandlers that are present in your classpath. -> eventHandler.autoDiscovery();");
        }
        if (eventManager.getDefaultEventHandler() == null) {
            throw new RuntimeException("Fatal: Twitch4J will not be functional unless you set a defaultEventHandler that can be used for internal events (eventManager.setDefaultEventHandler)!");
        }
    }

}
