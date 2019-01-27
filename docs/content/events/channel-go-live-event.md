+++
title="Channel - Go Live Event"
weight = 210
+++

# ChannelGoLiveEvent

## Description

The ChannelGoLiveEvent is triggered when a monitored channel goes live.

## Dependencies

Requires the specific channels to be registered with the TwitchClient Helper. See [TwitchClientHelper](../twitch4j/client-helper)

## Examples

### Example 1

```java
eventManager.onEvent(ChannelGoLiveEvent.class).subscribe(event -> {
	System.out.println("[" + event.getChannel().getName() + "] went live with title " + event.getTitle() + " on game " + event.getGameId() + "!");
});
```
