package me.philippheuer.twitch4j.api.helix.operations;

import me.philippheuer.twitch4j.api.helix.IHookServlet;
import me.philippheuer.twitch4j.api.helix.models.HelixStream;
import me.philippheuer.twitch4j.api.helix.models.HelixUser;

public interface WebHooksOperation {
	void listenFollow(HelixStream channel);
	void listenFollow(HelixUser channel);
//	void listenSubscription(Channel channel);
//	void listenSubscription(User channel);
	void listenStreamStatus(HelixStream channel);
//	void listenBits(Channel channel);
	void setServlet(IHookServlet hookServlet);
	IHookServlet getServlet();
}
