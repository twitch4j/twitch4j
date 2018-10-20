package twitch4j.helix;

public class TestUtils {

	public TwitchHelix getTwitchHelixClient() {
	    TwitchHelix client = TwitchHelixBuilder.builder().build();
		return client;
	}

}
