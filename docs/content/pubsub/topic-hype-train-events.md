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
```

### Listen for Hype Train Starts

```java
twitchClient.getEventManager().onEvent(HypeTrainStartEvent.class, System.out::println);
```

### Listen for Hype Train Progression

```java
twitchClient.getEventManager().onEvent(HypeTrainProgressionEvent.class, System.out::println);
```

### Listen for Hype Train Level Ups

```java
twitchClient.getEventManager().onEvent(HypeTrainLevelUpEvent.class, System.out::println);
```

### Listen for Hype Train Ends

```java
twitchClient.getEventManager().onEvent(HypeTrainEndEvent.class, System.out::println);
```

### Listen for Conductor Updates

```java
twitchClient.getEventManager().onEvent(HypeTrainConductorUpdateEvent.class, System.out::println);
```

### Listen for Cooldown Expiration

```java
twitchClient.getEventManager().onEvent(HypeTrainCooldownExpirationEvent.class, System.out::println);
```
