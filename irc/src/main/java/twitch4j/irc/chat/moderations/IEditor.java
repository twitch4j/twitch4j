package twitch4j.irc.chat.moderations;

import twitch4j.common.enums.CommercialType;

public interface IEditor {
	void startCommercial(CommercialType commercialType);

	void host(String channel);

	void unhost();

	void raid(String channel);

	void unraid();
}
