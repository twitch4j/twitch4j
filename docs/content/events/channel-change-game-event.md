+++
title="Channel - Change Game Event"
weight = 210
+++

# ChannelChangeGameEvent

## Description

The ChannelChangeGameEvent is triggered when a monitored channel changes the game.

## Dependencies

Requires the specific channels to be registered with the TwitchClient Helper. See [TwitchClientHelper](../twitch4j/client-helper)

## Examples

### Example 1

```java
eventManager.onEvent(ChannelChangeGameEvent.class).subscribe(event -> {
	System.out.println("[" + event.getChannel().getName() + "] is now playing " + event.getGameId() + "!");
});
```
