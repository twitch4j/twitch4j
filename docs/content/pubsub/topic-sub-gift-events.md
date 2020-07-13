+++
title="Topic - Sub Gift Events"
weight = 50
+++

# Topic - Sub Gift Events

## Description

This topic can be used to monitor sub gift activity in a specified channel.

Unlike the documented Channel Subscriptions topic (fires many events upon a multi-target gift), this allows one to know how many subs were gifted in a single event.
The documented analog to this topic is in the `TwitchChat` module.

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | Auth Token (may not necessarily be required) |
| channelId | String | Target Channel Id |

## Code-Snippets

Subscribe to all sub gift events in channel `twitch4j` and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForChannelSubGiftsEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelSubGiftEvent.class, System.out::println);
```
