+++
title="Channel - MessageEvent"
weight = 210
+++

# ChannelMessageEvent

## Description

The ChannelMessageEvent is triggered for every normal message in irc.

## Dependencies

Requires the TwitchChat Module to be enabled and active for the source channel. See [TwitchChat](../chat).

## Examples

### Write Chat to Console

```java
eventManager.onEvent(ChannelMessageEvent.class).subscribe(event -> {
	System.out.println("[" + event.getChannel().getName() + "] " + event.getUser().getName() + ": " + event.getMessage());
});
```
