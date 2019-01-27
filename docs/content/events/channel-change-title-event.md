+++
title="Channel - Change Title Event"
weight = 210
+++

# ChannelChangeTitleEvent

## Description

The ChannelChangeTitleEvent is triggered when a monitored channel changes it's title.

## Dependencies

Requires the specific channels to be registered with the TwitchClient Helper. See [TwitchClientHelper](../twitch4j/client-helper)

## Examples

### Example 1

```java
eventManager.onEvent(ChannelChangeTitleEvent.class).subscribe(event -> {
	System.out.println("[" + event.getChannel().getName() + "] changed his stream title to " + event.getTitle() + "!");
});
```
