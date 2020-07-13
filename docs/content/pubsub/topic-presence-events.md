+++
title="Topic - Presence Events"
weight = 50
+++

# Topic - Presence Events

## Description

This topic can be used to monitor the presence of a specified user's friends (and oneself, to an extent).

Example availabilities include:
* `busy`
* `idle`
* `offline`
* `online`

Example activities include:
* `none`
* `watching`
* `broadcasting`

With all undocumented topics, use at your own risk.

## Method Definition

| Name		  | Type	  | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target user id, with the scope `user_presence_friends_read` and/or `user_presence_edit` |
| userId | String | Target User Id |

## Code-Snippets

### Example: General use

Subscribe to presence events for user `twitch4j` and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForPresenceEvents(credential, "149223493");

// Listen for presence updates
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(UserPresenceEvent.class, System.out::println);

// Listen for updates to the user's presence settings
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(PresenceSettingsEvent.class, System.out::println);
```

### Example: Live status monitoring

Note: a single account can befriend 500 users on Twitch (which is necessary for such presence events to occur), at the time of writing.

Note: these events tend to be fired significantly faster than the documented analogs in the Helix API.

Disclaimer: do not solely rely upon this code; fallback mechanisms should be employed as well.

```java
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(UserPresenceEvent.class, e -> {
	boolean wentLive = e.getData().getActivities().stream().anyMatch(a -> "broadcasting".equalsIgnoreCase(a.getType()));
	if (wentLive) {
		System.out.println(e); // Handle Go Live
	}
});
```
