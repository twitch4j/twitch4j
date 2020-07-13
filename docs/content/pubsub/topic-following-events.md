+++
title="Topic - Following Events"
weight = 50
+++

# Topic - Following Events

## Description

This topic can be used to monitor whenever a specified channel receives followers.

In terms of documented replacements, the User Follows Webhook provides another real-time system to receive these events.
If a webhook server cannot be used, one can also query API Helix > Users Followers at a regular interval to determine new followers. In fact, Twitch4J > Client Helper can do this for you.

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | Auth Token (may not necessarily be required) |
| channelId | String | Target Channel Id |

## Code-Snippets

Subscribe to new follower events for user twitch4j and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForFollowingEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(FollowingEvent.class, System.out::println);
```
