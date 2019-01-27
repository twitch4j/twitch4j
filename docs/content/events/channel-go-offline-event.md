+++
title="Channel - Go Offline Event"
weight = 210
+++

# ChannelGoOfflineEvent

## Description

The ChannelGoOfflineEvent is triggered when a monitored channel goes offline.

## Dependencies

Requires the specific channels to be registered with the TwitchClient Helper. See [TwitchClientHelper](../twitch4j/client-helper)

## Examples

### Example 1

```java
eventManager.onEvent(ChannelGoOfflineEvent.class).subscribe(event -> {
	System.out.println("[" + event.getChannel().getName() + "] just went offline!");
});
```
