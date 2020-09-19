+++
title="Topic - Subscribe Events"
weight = 50
+++

# Topic - Subscribe Events

## Description

This topic can be used to monitor whenever a specified channel receives a new subscription.

## Method Definition

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target channel id, with the scope `channel_subscriptions` |
| channelId | String | Target Channel Id |

*Optional Parameters*

None

## Code-Snippets

Subscribe to all subscription events to the twitch4j channel and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForSubscriptionEvents(credential, "149223493");

twitchClient.getEventManager().onEvent(ChannelSubscribeEvent.class, System.out::println);
```
