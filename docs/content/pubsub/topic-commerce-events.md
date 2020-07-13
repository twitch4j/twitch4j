+++
title="Topic - Commerce Events"
weight = 50
+++

# Topic - Commerce Events

## Description

This topic is deprecated by Twitch, but it could be used to monitor purchases in a specified channel. 

## Method Definition

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target user id, with any scope |
| userId | String | Target User Id |

*Optional Parameters*

None

## Code-Snippets

Subscribe to all commerce purchases in the twitch4j channel and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForCommerceEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelCommerceEvent.class, System.out::println);
```
