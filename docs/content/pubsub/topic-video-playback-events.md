+++
title="Topic - Video Playback Events"
weight = 50
+++

# Topic - Video Playback Events

## Description

This topic can be used to monitor video playback events for a specified channel.

Subtypes include:
* Commercial started
* Stream went down
* Stream went up
* View count update

Note: The stream up/down events tend to be fired significantly faster than what is reported by Helix.
Note: the view count reported by this topic is more reflective of the value on the website than API - TMI > Get Viewers

With all undocumented topics, use at your own risk.

## Method Definition

### By Channel ID (Preferred)

| Name		  | Type	  | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | Auth Token (may not necessarily be required) |
| channelId | String | Target Channel Id |

### By Channel Name (Alternative)

| Name		  | Type	  | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | Auth Token (may not necessarily be required) |
| channelName | String | Target Channel Name |

## Code-Snippets

### Subscribe to topic

#### By Channel ID

**Preferred** way to subscribe to the topic; used by first-party clients and more resistant to name weirdness.

```java
twitchClient.getPubSub().listenForVideoPlaybackEvents(credential, "149223493");
```

#### By Channel Name

An _alternative_ means to subscribe to the topic, if one does not have the channel ID and does not wish to query it.

```java
twitchClient.getPubSub().listenForVideoPlaybackByNameEvents(credential, "twitch4j");
```

### Listen for Events

```java
// Handle all subtypes
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(VideoPlaybackEvent.class, System.out::println);

// Alternatively, only consider a specific subtype
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(VideoPlaybackEvent.class, e -> {
	if (e.getData().getType() == VideoPlaybackData.Type.STREAM_UP) {
		System.out.println(e); // Handle Go Live
	}
});
```
