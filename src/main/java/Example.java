import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.endpoints.*;

public class Example {

	public static void main(String[] args) {
		String applicationClientId = "1xveh3z5kajmnnobon6j5r5drb98e4";
		
		Twitch4J twitch4J = new Twitch4J(applicationClientId, "dummy");
		
		UserEndpoint user = new UserEndpoint(twitch4J, "a_seagull");
		
		
		
		
		GetChannelById getChannel = new GetChannelById(twitch4J, "19070311"); // A_Seagull [https://api.twitch.tv/kraken/users/A_seagull?client_id=1xveh3z5kajmnnobon6j5r5drb98e4]
		
		GetChannelFollows getChannelFollows = new GetChannelFollows(twitch4J, "19070311"); // A_Seagull [https://api.twitch.tv/kraken/users/A_seagull?client_id=1xveh3z5kajmnnobon6j5r5drb98e4]
		
	}

}
