package twitch4j.irc.chat.moderations;

import lombok.RequiredArgsConstructor;
import twitch4j.common.enums.CommercialType;
import twitch4j.irc.chat.IChannel;

@RequiredArgsConstructor
public class EditorImpl implements IEditor {
	private final IChannel channel;

	@Override
	public void startCommercial(CommercialType commercialType) {
		channel.sendMessage(String.format("/commercial %d", commercialType.getSeconds()));
	}

	@Override
	public void host(String channel) {
		this.channel.sendMessage(String.format("/host %s", channel));
	}

	@Override
	public void unhost() {
		this.channel.sendMessage("/unhost");
	}

	/**
	 * @param channel
	 * @see <a href="https://help.twitch.tv/customer/portal/articles/2877820">Raids</a>
	 */
	@Override
	public void raid(String channel) {
		this.channel.sendMessage(String.format("/raid %s", channel));
	}

	/**
	 * @see <a href="https://help.twitch.tv/customer/portal/articles/2877820">Raids</a>
	 */
	@Override
	public void unraid() {
		this.channel.sendMessage("/unraid");
	}
}
