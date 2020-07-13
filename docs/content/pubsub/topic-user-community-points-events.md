+++
title="Topic - User Community Points Events"
weight = 50
+++

# Topic - User Community Points Events

## Description

This topic can be used to monitor events regarding a specified user's community points.

The topic is analogous to the documented PubSub > Channel Points Events, but from the perspective of the user (who may be in multiple channels).

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target user id (scope undocumented) |
| userId | String | Target User Id |

## Code-Snippets

Subscribe to all user community points events for `twitch4j` and register a listener that prints all messages to console

```java
// Subscribe to topic
twitchClient.getPubSub().listenForUserChannelPointsEvents(credential, "149223493");

// Register event listeners
SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
eventHandler.onEvent(PointsEarnedEvent.class, System.out::println);
eventHandler.onEvent(ClaimAvailableEvent.class, System.out::println);
eventHandler.onEvent(ClaimClaimedEvent.class, System.out::println);
eventHandler.onEvent(PointsSpentEvent.class, System.out::println);
eventHandler.onEvent(RewardRedeemedEvent.class, System.out::println);
```
