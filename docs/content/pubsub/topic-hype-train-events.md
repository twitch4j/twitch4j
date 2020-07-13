+++
title="Topic - Hype Train Events"
weight = 50
+++

# Topic - Hype Train Events

## Description

This topic can be used to monitor hype train events for a specified channel.

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | Auth Token (may not necessarily be required) |
| channelId | String | Target Channel Id |

## Code-Snippets

### Subscribe to the topic for a given channel

```java
twitchClient.getPubSub().listenForHypeTrainEvents(credential, "149223493");

SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
```

### Listen for Hype Train Starts

```java
eventHandler.onEvent(HypeTrainStartEvent.class, System.out::println);
```

### Listen for Hype Train Progression

```java
eventHandler.onEvent(HypeTrainProgressionEvent.class, System.out::println);
```

### Listen for Hype Train Level Ups

```java
eventHandler.onEvent(HypeTrainLevelUpEvent.class, System.out::println);
```

### Listen for Hype Train Ends

```java
eventHandler.onEvent(HypeTrainEndEvent.class, System.out::println);
```

### Listen for Conductor Updates

```java
eventHandler.onEvent(HypeTrainConductorUpdateEvent.class, System.out::println);
```

### Listen for Cooldown Expiration

```java
eventHandler.onEvent(HypeTrainCooldownExpirationEvent.class, System.out::println);
```
