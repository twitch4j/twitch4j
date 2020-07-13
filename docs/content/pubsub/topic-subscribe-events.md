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
| credential | OAuth2Credential | User Auth Token for the target user id, with the scope `channel_subscriptions` |
| userId | String | Target User Id |

*Optional Parameters*

None

## Code-Snippets

Subscribe to all subscription events to the twitch4j channel and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForSubscriptionEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelSubscribeEvent.class, System.out::println);
```
