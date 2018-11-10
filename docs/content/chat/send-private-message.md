+++
title="Send Private Message"
weight = 203
+++

# Send Private Message

## Description

Sends a private message to a user.

* The messages will go into a queue internally so we won't hit the rate-limit.
* This could cause some minor deleays when sending a lot of messages.
* The queue will only be processed when we're connected to twitch.

## Example

```java
twitchClient.getChat().sendPrivateMessage("twitch4j", "Hey! This is a direct message.");
```
