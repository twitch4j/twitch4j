package com.github.twitch4j.modules.event;

import com.github.twitch4j.modules.IModule;

/**
 * Module Disabled Event
 */
public class ModuleDisabledEvent extends ModuleEvent {

    /**
     * Constructor
     *
     * @param module Module
     */
	public ModuleDisabledEvent(IModule module) {
		super(module);
	}
}
