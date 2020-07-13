+++
title="Topic - Whispers"
weight = 50
+++

# Topic - Whispers

## Description

This topic can be used to monitor whenever a specified user receives a whisper.

Note that this can cause duplication of `PrivateMessageEvent` if one is also using `TwitchChat` to monitor whispers.

## Method Definition

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target user id, with the scope `whispers:read` |
| userId | String | Target User Id |

*Optional Parameters*

None

## Code-Snippets

Subscribe to all whispers to user twitch4j and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForWhisperEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(PrivateMessageEvent.class, System.out::println);
```
