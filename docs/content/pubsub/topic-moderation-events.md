+++
title="Topic - Moderation Events"
weight = 50
+++

# Topic - Moderation Events

## Description

This topic can be used to monitor whenever a moderation performs an action in the specified channel.

## Method Definition

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target user id, with the scope `channel:moderate` |
| userId | String | Moderator User Id |
| channelId | String | Target Channel Id |

*Optional Parameters*

None

## Code-Snippets

Example: User `hexafice` subscribes to moderation events in channel `twitch4j`

```java
// Listen to topic
String userId = "142621956"; // user id of hexafice
String broadcasterId = "149223493"; // channel id of twitch4j
twitchClient.getPubSub().listenForModerationEvents(credential, userId, broadcasterId);

// Handle events
twitchClient.getEventManager().onEvent(ChatModerationEvent.class, System.out::println);
```
