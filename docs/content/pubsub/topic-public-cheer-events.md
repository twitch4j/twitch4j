+++
title="Topic - Public Cheer Events"
weight = 50
+++

# Topic - Public Cheer Events

## Description

This topic can be used to monitor public cheer events for a specified channel. Currently, this only represents Cheerbombs (i.e. a cheer with shared rewards associated).

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | Auth Token (may not necessarily be required) |
| channelId | String | Target Channel Id |

## Code-Snippets

Subscribe to all cheerbombs in channel `twitch4j` and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForPublicCheerEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(CheerbombEvent.class, System.out::println);
```
