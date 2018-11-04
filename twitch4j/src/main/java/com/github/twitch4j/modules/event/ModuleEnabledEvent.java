package com.github.twitch4j.modules.event;

import com.github.twitch4j.modules.IModule;

public class ModuleEnabledEvent extends ModuleEvent {
	public ModuleEnabledEvent(IModule module) {
		super(module);
	}
}
