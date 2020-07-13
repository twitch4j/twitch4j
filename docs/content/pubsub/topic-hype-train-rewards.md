+++
title="Topic - Hype Train Rewards"
weight = 50
+++

# Topic - Hype Train Rewards

## Description

This topic can be used to monitor the distribution of rewards associated with hype trains for a given channel.

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token (may not necessarily be required) |
| channelId | String | Target Channel Id |

## Code-Snippets

Subscribe to hype train rewards for channel `twitch4j` and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForHypeTrainRewardEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(HypeTrainRewardsEvent.class, System.out::println);
```
