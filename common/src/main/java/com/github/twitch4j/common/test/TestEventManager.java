package com.github.twitch4j.common.test;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestEventManager extends EventManager {
    private final List<Object> publishedEvents = Collections.synchronizedList(new ArrayList<>());

    public TestEventManager() {
        super();
        autoDiscovery();
        setDefaultEventHandler(SimpleEventHandler.class);
    }

    @Override
    public void publish(Object event) {
        publishedEvents.add(event);
        super.publish(event);
    }

    public List<Object> getPublishedEvents() {
        return publishedEvents;
    }

}
