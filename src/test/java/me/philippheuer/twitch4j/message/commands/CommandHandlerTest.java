package me.philippheuer.twitch4j.message.commands;

import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;

public class CommandHandlerTest extends TwitchClientIntegrationTest {

	/**
	 * Regression test for issue 35
	 * @see <a href="https://github.com/twitch4j/twitch4j/issues/35">Issue</a>
	 */
	public void testRegressionIssue35() {
		//Test will fail if an exception is thrown

		TestCommand command = new TestCommand();

		//Configuration autosave is disabled by default
		//According to issue 35, this should throw a NullPointerException
		twitchClient.getCommandHandler().registerCommand(command);
	}

}
