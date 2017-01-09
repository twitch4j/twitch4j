import java.util.List;

import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.endpoints.*;
import de.philippheuer.twitch4j.model.*;

public class Example {

	public static void main(String[] args) {
		String applicationClientId = "1xveh3z5kajmnnobon6j5r5drb98e4";
		
		Twitch4J twitch4J = new Twitch4J(applicationClientId, "dummy");
		
		// Get UserId From UserName
		Long channelId = null;
		try {
			UserEndpoint userEndpoint = twitch4J.getUserEndpoint();
			channelId = userEndpoint.getUserIdByUserName("a_seagull");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Get Channel Endpoint
		try {
			ChannelEndpoint channelEndpoint = twitch4J.getChannelEndpoint(channelId);
			
			List<Follow> follows = channelEndpoint.getFollowers();
			for(Follow follow : follows) {
				System.out.println(String.format("User %s followed at %s", follow.getUser().getName(), follow.getCreatedAt().toString()));
			}
			
			List<Subscription> subs = channelEndpoint.getSubscriptions();
			for(Subscription sub : subs) {
				System.out.println(String.format("User %s followed at %s", sub.getUser().getName(), sub.getCreatedAt().toString()));
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
