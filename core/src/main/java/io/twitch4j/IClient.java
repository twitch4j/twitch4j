package io.twitch4j;

import io.twitch4j.auth.IManager;
import io.twitch4j.events.IDispatcher;
import io.twitch4j.modules.IModuleLoader;

public interface IClient extends ISocket {
    IConfiguration getConfiguration();

    IManager getCredentialManager();

    IModuleLoader getModuleLoader();

    IDispatcher getDispatcher();
}
