+++
title="Topic - Friendship Events"
weight = 50
+++

# Topic - Friendship Events

## Description

This topic can be used to monitor changes to the friendship state of a specified user with others.

Possible friendship changes include:
* Requested
* Rejected
* Accepted
* Removed
* and self variants (i.e. when this account itself is responsible for the change rather than the other entity)

With all undocumented topics, use at your own risk.

## Method Definition

| Name          | Type      | Description  |
| ------------- |:---------:| -----------------:|
| credential | OAuth2Credential | User Auth Token for the target user id, with the scope `user_friends_read` |
| userId | String | Target User Id |

## Code-Snippets

Subscribe to friendship events for user twitch4j and register a listener that prints all messages to console

```java
twitchClient.getPubSub().listenForFriendshipEvents(credential, "149223493");

twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(FriendshipEvent.class, System.out::println);
```
