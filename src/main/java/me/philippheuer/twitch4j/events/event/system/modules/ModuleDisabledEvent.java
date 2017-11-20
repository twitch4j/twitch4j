package me.philippheuer.twitch4j.events.event.system.modules;

import me.philippheuer.twitch4j.modules.IModule;

public class ModuleDisabledEvent extends ModuleEvent {
	public ModuleDisabledEvent(IModule module) {
		super(module);
	}
}
