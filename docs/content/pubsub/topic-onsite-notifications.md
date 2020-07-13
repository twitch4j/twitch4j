+++
title="Topic - Onsite Notifications"
weight = 50
+++

# Topic - Onsite Notifications

## Description

This topic can be used to monitor notifications that would occur on the twitch website (e.g. if a stream goes live).

With all undocumented topics, use at your own risk.

## Method Definition

| Name		  | Type	  | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target user id |
| userId | String | Target User Id |

## Code-Snippets

### Example: General use

Subscribe to all onsite notifications for user `twitch4j` and register listeners that print all messages to console

```java
// Subscribe to the topic
twitchClient.getPubSub().listenForOnsiteNotificationEvents(credential, "149223493");

// Listen to notification creations
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(OnsiteNotificationCreationEvent.class, System.out::println);

// Listen for notification summary updates
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(UpdateOnsiteNotificationSummaryEvent.class, System.out::println);
```

### Example: Live status monitoring

Note: a single channel can be following up to 2000 channels, as of the time of writing. Such events cannot be fired unless the notifications bell is enabled for the channel.

Note: these notifications tend to be fired faster than repeatedly querying API - Helix > Streams Get, as `TwitchClientHelper` (Twitch4J > Client Helper) does.

Disclaimer: do not solely rely upon this code; fallback mechanisms should be employed as well.

```java
// Establish which channels' live state is relevant
Set<String> interestedChannelIds = new HashSet<>();
interestedChannelIds.add("12826");
interestedChannelIds.add("53888434");
interestedChannelIds.add("141981764");
interestedChannelIds.add("142621956");

// Subscribe to the topic
twitchClient.getPubSub().listenForOnsiteNotificationEvents(credential, userId);

// Listen to notification creations
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(OnsiteNotificationCreationEvent.class, e -> {
	OnsiteNotification notification = e.getData().getNotification();
	if ("streamup".equalsIgnoreCase(notification.getType())) {
		List<OnsiteNotification.Creator> creators = notification.getCreators();
		if (creators != null && creators.size() == 1 && interestedChannelIds.contains(creators.get(0).getUserId())) {
			System.out.println(e); // Handle Go Live
		}
	}
});
```
