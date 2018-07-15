package twitch4j.modules.event;

import twitch4j.modules.IModule;

public class ModuleDisabledEvent extends ModuleEvent {
	public ModuleDisabledEvent(IModule module) {
		super(module);
	}
}
