+++
title="Topic - Bits Events"
weight = 50
+++

# Topic - Bits Events

## Description

This topic can be used to monitor whenever bits are cheered in a specified channel.

## Method Definition

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target user id, with the scope `bits:read` |
| userId | String | Target User Id |

*Optional Parameters*

None

## Code-Snippets

Subscribe to all cheers to user twitch4j and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForCheerEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelBitsEvent.class, System.out::println);
```
