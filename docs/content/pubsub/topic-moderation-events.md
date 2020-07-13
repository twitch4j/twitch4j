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
| channelId | String | Target Channel Id |

*Optional Parameters*

None

## Code-Snippets

### Official

Subscribe to all moderation events in the twitch4j channel and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForModerationEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChatModerationEvent.class, System.out::println);
```

### Unofficial

It is also possible to subscribe to the previously-documented version of this topic by providing a user id in addition to the channel id.
This version of the topic has reduced oauth requirements, which allows moderators to subscribe to the topic without having the broadcaster's user access token.
Also, it is possible for users without elevated permissions to monitor any moderation actions that are specifically applicable to their own user account (and not receive others).
As this is not officially documented by Twitch, it may break at any time (however, at the time of writing, this version of the topic is actively used by first-party clients).

Example: User `hexafice` subscribes to moderation events in channel `twitch4j`

```java
twitchClient.getPubSub().listenForModerationEvents(credential, "149223493", "142621956");
```
