package twitch4j.helix;

public class TestUtils {

	public TwitchHelix getTwitchHelixClient() {
	    TwitchHelix client = new TwitchHelixBuilder().build();
		return client;
	}

}
