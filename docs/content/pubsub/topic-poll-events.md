+++
title="Topic - Poll Events"
weight = 50
+++

# Topic - Poll Events

## Description

This topic can be used to monitor poll events in a specified channel.

Sub-types include:
* Poll creation
* Poll update (e.g. new vote or bits usage or channel points towards a specific choice)
* Poll completion
* Poll termination
* Poll archived

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | Auth Token (may not necessarily be required) |
| channelId | String | Target Channel Id |

## Code-Snippets

Subscribe to all poll events in channel `twitch4j` and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForPollEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(PollsEvent.class, System.out::println);
```
