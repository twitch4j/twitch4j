+++
title="Topic - Raid Events"
weight = 50
+++

# Topic - Raid Events

## Description

This topic can be used to monitor raid events for a specified channel. This does not refer to the specified channel *getting* raided, only actions taken by the channel itself (or its editors).

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | Auth Token (may not necessarily be required) |
| channelId | String | Target Channel Id |

## Code-Snippets

Subscribe to all raid events in channel `twitch4j` and register a listener that prints all messages to console

```java
// Subscribe to the topic
twitchClient.getPubSub().listenForRaidEvents(credential, "149223493");

// Listen for the channel executing a raid
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(RaidGoEvent.class, System.out::println);

// Listen for raid progress (counting down until the raid can go through)
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(RaidUpdateEvent.class, System.out::println);

// Listen for raid cancellations
twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(RaidCancelEvent.class, System.out::println);
```
