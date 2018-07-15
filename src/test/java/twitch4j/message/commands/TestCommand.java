package twitch4j.message.commands;

import twitch4j.events.event.irc.ChannelMessageEvent;

public class TestCommand extends Command {

	public TestCommand() {
		setCommand("test");
		setCategory("test");
		getRequiredPermissions().add(CommandPermission.EVERYONE);
	}

	@Override
	public void executeCommand(ChannelMessageEvent event) {
		System.out.println("This is a test");
	}
}
