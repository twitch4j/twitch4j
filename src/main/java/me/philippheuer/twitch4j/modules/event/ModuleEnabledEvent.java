package me.philippheuer.twitch4j.modules.event;

import me.philippheuer.twitch4j.modules.IModule;

public class ModuleEnabledEvent extends ModuleEvent {
	public ModuleEnabledEvent(IModule module) {
		super(module);
	}
}
