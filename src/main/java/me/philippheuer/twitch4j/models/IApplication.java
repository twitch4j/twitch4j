package me.philippheuer.twitch4j.models;

import me.philippheuer.twitch4j.IAuthorization;

public interface IApplication {
	String getClientId();
	String getClientSecret();
	IAuthorization getBotAuthorization();
	void setBotAuthorization(IAuthorization botAuthorization);
}
