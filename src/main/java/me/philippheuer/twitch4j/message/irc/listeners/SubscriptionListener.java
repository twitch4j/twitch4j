package me.philippheuer.twitch4j.message.irc.listeners;

import me.philippheuer.twitch4j.enums.SubPlan;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.ChannelMessageEvent;
import me.philippheuer.twitch4j.events.event.IrcRawMessageEvent;
import me.philippheuer.twitch4j.events.event.SubscriptionEvent;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.Subscription;
import me.philippheuer.twitch4j.model.User;

import java.util.Date;
import java.util.Optional;

/**
 * Channel Message Listener
 *
 * Listens for normal message in a channel
 */
public class SubscriptionListener {

	@EventSubscriber
	public void onRawIrcMessage(IrcRawMessageEvent event) {
		if(event.getIrcParser().getCommand().equals("USERNOTICE") && event.getIrcParser().getTags().hasTag("msg-id")) {
			if(event.getIrcParser().getTags().getTag("msg-id").toString().equalsIgnoreCase("sub") || event.getIrcParser().getTags().getTag("msg-id").toString().equalsIgnoreCase("resub")) {
				// Load User Info
				Channel channel = event.getClient().getChannelEndpoint(event.getIrcParser().getChannelName()).getChannel();
				User user = event.getClient().getUserEndpoint().getUser(event.getIrcParser().getUserId()).get();
				String subPlan = event.getIrcParser().getTags().getTag("msg-param-sub-plan").toString();
				boolean isResub = event.getIrcParser().getTags().getTag("msg-id").toString().equalsIgnoreCase("resub");
				Integer subStreak = Integer.parseInt(event.getIrcParser().getTags().getTag("msg-param-months").toString());

				// Twitch sometimes returns 0 months for new subs
				if(subStreak == 0) {
					subStreak = 1;
				}

				// Build Subscription Entity
				Subscription entity = new Subscription();
				entity.setCreatedAt(new Date());
				entity.setMessage(event.getIrcParser().getMessage() != null ? event.getIrcParser().getMessage() : ""); // You can't write a message for the first sub.
				entity.setStreak(subStreak);
				entity.setUser(user);
				entity.setSubPlanByCode(subPlan);

				// Dispatch Event
				SubscriptionEvent subEvent = new SubscriptionEvent(channel, entity);
				event.getClient().getDispatcher().dispatch(subEvent);
			}
		}
	}

}
