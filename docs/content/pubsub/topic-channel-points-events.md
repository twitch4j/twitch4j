+++
title="Topic - Channel Points Events"
weight = 50
+++

# Topic - Channel Points Events

## Description

This topic can be used to monitor a channel's community points events.
The only officially documented subtype of this event is for a custom reward being redeemed, but this library also includes other subtypes.

## Method Definition

*Required Parameters*

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target channel id, with the scope `channel:read:redemptions` |
| channelId | String | Target Channel Id |

*Optional Parameters*

None

## Code-Snippets

### Subscribe to the topic for a given channel

```java
twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, "149223493");

SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
```

### Listen for Custom Reward Redemptions

Fired when a _custom_ reward is redeemed in the channel.

```java
eventHandler.onEvent(RewardRedeemedEvent.class, System.out::println);
```

### Listen for Redemption Status Updates

Not documented by Twitch.

Fired when the status of a redemption changes (e.g. completed or rejected). 

Note that, at the time of writing, the status is `ACTION_TAKEN` whether the reward was completed or rejected, rather than `FULFILLED` or `UNFULFILLED`.

```java
eventHandler.onEvent(RedemptionStatusUpdateEvent.class, System.out::println);
```

### Listen for Reward Creations

Not documented by Twitch.

Fired when a _custom_ reward is **created**.

```java
eventHandler.onEvent(CustomRewardCreatedEvent.class, System.out::println);
```

### Listen for Reward Updates

Not documented by Twitch.

Fired when a _custom_ reward is **updated**.

```java
eventHandler.onEvent(CustomRewardUpdatedEvent.class, System.out::println);
```

### Listen for Reward Deletions

Not documented by Twitch.

Fired when _custom_ reward is **deleted**.

```java
eventHandler.onEvent(CustomRewardDeletedEvent.class, System.out::println);
```

### Listen for Update Redemption Status Progress

Not documented by Twitch.

Fired when there is an update to the redemption progress.

```java
eventHandler.onEvent(UpdateRedemptionProgressEvent.class, System.out::println);
```

### Listen for Update Redemption Status Completion

Not documented by Twitch.

Fired when the redemption progress has completed.

```java
eventHandler.onEvent(UpdateRedemptionFinishedEvent.class, System.out::println);
```
